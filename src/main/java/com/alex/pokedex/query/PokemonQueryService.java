package com.alex.pokedex.query;

import com.alex.pokedex.dto.*;
import com.alex.pokedex.mapper.PokemonMapper;
import com.alex.pokedex.model.Pokemon;
import com.alex.pokedex.repository.PokemonRepository;
import com.alex.pokedex.service.PokeApiService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.alex.pokedex.util.PokemonConstants.*;

@Component
@AllArgsConstructor
public class PokemonQueryService {

    private final PokemonRepository pokemonRepository;
    private final PokeApiService pokeApiService;

    private final PokemonMapper pokemonMapper;


    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();


    private final RestClient restClient = RestClient.builder()
            .baseUrl(POKEAPI_BASE_URL)
            .build();

    public List<Pokemon> saveAll(List<Pokemon> pokemons) {
        return pokemonRepository.saveAll(pokemons);
    }

    public Pokemon getPokemonById(Long Id) {
        return pokemonRepository.findById(Id).orElseThrow();
    }

    public List<Pokemon>getPokemonsWithPagination(int offset, int limit){
        PokeApiResponseDto response = pokeApiService.getPokemonsWithPagination(offset, limit);
        List<PokemonDetailsDto> getPokemonDetails = getPokemonDetailsInParallel(response.results());

        List<Pokemon> pokemonsMapped = pokemonMapper.toEntities(getPokemonDetails);

        List<Pokemon> pokemonsWithDescription =
        pokemonsMapped.stream()
                .map(e-> {
                    e.setChain(
                            pokemonMapper.toChainEntity(pokeApiService.getLinage(e.getId().intValue()))
                    );
                    e.setFlavorTextEntries(
                        pokemonMapper.toStatsEntity(pokeApiService.getNarrativeDescription(e.getId().intValue()))

                    );
                    return e;
                })
                .toList();


        return pokemonsWithDescription;
    }

    public List<PokemonDetailsDto> getPokemonDetailsInParallel(List<PokemonResultDto> listPokemon) {

        List<CompletableFuture<PokemonDetailsDto>> futures = listPokemon.stream()
                .map(pokemon -> CompletableFuture.supplyAsync(() -> {
                    try {
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(pokemon.url()))
                                .GET()
                                .build();

                        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                        return objectMapper.readValue(response.body(), PokemonDetailsDto.class);

                    } catch (Exception e) {
                        System.err.println("Error to get Pokemon details of " + pokemon.name() + ": " + e.getMessage());
                        return null;
                    }
                }))
                .toList();

        CompletableFuture<Void> todasLasPeticiones = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        return todasLasPeticiones.thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .filter(pokemon -> pokemon != null)
                        .toList())
                .join();
    }

    public List<PokemonDescriptionDTO> getNarrativeDescriptionInParallel(List<PokemonDetailsDto> listPokemon) {

        List<CompletableFuture<PokemonDescriptionDTO>> futures = listPokemon.stream()
                .map(pokemon -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return restClient.get()
                                .uri(uriBuilder -> uriBuilder
                                        .path("/pokemon-species/{id}")
                                        .build(pokemon.id()))
                                .retrieve()
                                .body(PokemonDescriptionDTO.class);

                    } catch (Exception e) {
                        System.err.println("Error to get Pokemon description of " + pokemon.id() + ": " + e.getMessage());
                        return null;
                    }
                }))
                .toList();

        CompletableFuture<Void> todasLasPeticiones = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        return todasLasPeticiones.thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .filter(dto -> dto != null)
                        .toList())
                .join();
    }
}

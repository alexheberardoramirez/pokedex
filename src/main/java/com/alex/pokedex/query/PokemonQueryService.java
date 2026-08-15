package com.alex.pokedex.query;

import com.alex.pokedex.dto.*;
import com.alex.pokedex.mapper.PokemonMapper;
import com.alex.pokedex.model.Pokemon;
import com.alex.pokedex.repository.PokemonRepository;
import com.alex.pokedex.service.PokeApiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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

    public List<Pokemon> findAll() {
        log.info("Query DB");
        List<Pokemon> pokemons = pokemonRepository.findAll();
        log.info("Query DB successfully");
        return pokemons;
    }

    public boolean existsById(int Id) {
        return pokemonRepository.existsById(Long.valueOf(Id));
    }

    public Pokemon getPokemonById(Long Id) {
        log.info("Query DB");
        Pokemon pokemon = pokemonRepository.findById(Id).orElseThrow();
        log.info("Query DB successfully");
        return pokemon;
    }

    public List<Pokemon>getPokemonsWithPagination(int offset, int limit){
        log.info("Calling pokemon API");
        PokeApiResponseDto response = pokeApiService.getPokemonsWithPagination(offset, limit);
        log.info("Get response successfully");
        log.info("Calling pokemon API to get pokemon Details");
        List<PokemonDetailsDto> getPokemonDetails = getPokemonDetailsInParallel(response.results());
        log.info("Get pokemon details response successfully");
        List<Pokemon> pokemonsMapped = pokemonMapper.toEntities(getPokemonDetails);
        List<Pokemon> pokemonsWithDescription =
        pokemonsMapped.stream()
                .map(e-> {
                    log.info("Calling pokemon API to get Linage");
                    e.setChain(
                            pokemonMapper.toChainEntity(pokeApiService.getLinage(e.getId().intValue()))
                    );
                    log.info("Get pokemon linage response successfully");
                    log.info("Calling pokemon API to get Description");
                    e.setFlavorTextEntries(
                        pokemonMapper.toStatsEntity(pokeApiService.getNarrativeDescription(e.getId().intValue()))

                    );
                    log.info("Get pokemon description response successfully");
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

}

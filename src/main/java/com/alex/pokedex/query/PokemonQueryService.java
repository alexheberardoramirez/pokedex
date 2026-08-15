package com.alex.pokedex.query;

import com.alex.pokedex.dto.*;
import com.alex.pokedex.model.Pokemon;
import com.alex.pokedex.repository.PokemonRepository;
import com.alex.pokedex.service.PokeApiService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@AllArgsConstructor
public class PokemonQueryService {

    private final PokemonRepository pokemonRepository;
    private final PokeApiService pokeApiService;


    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();


    public List<Pokemon> saveAll(List<Pokemon> pokemons) {
        return pokemonRepository.saveAll(pokemons);
    }

    public Pokemon getPokemonById(Long Id) {
        return pokemonRepository.findById(Id).orElseThrow();
    }

    public List<PokemonDetailsDto> getPokemonsWithPagination(int offset, int limit){
        PokeApiResponseDto response = pokeApiService.getPokemonsWithPagination(offset, limit);
        List<PokemonDetailsDto> getPokemonDetails = getPokemonDetailsInParallel(response.results());
        PokeAPISpecieResponseDTO pokeAPISpecieResponseDTO =pokeApiService.getNarrativeDescription(1);
        PokeApiLinageChainResponseDTO pokeApiLinageChainResponseDTO =pokeApiService.getLinage(1);
        System.out.println();

        return getPokemonDetails;
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

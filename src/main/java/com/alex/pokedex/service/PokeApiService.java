package com.alex.pokedex.service;

import com.alex.pokedex.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

import static com.alex.pokedex.util.PokemonConstants.*;

@Service
public class PokeApiService {

    private final RestClient restClient = RestClient.builder()
            .baseUrl(POKEAPI_BASE_URL)
            .build();

    public PokemonDetailsDto getPokemonById(int id) {
        PokemonDetailsDto pokemonDetailsDto =
                restClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path(POKEAPI_PATH + "/{id}")
                                .build(id))
                        .retrieve()
                        .body(PokemonDetailsDto.class);

        return pokemonDetailsDto;
    }

    public List<FlavorTextEntriesDTO> getNarrativeDescription(int id) {
        PokeAPISpecieResponseDTO flavorTextEntriesDTO =
        restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(POKE_SPECIESAPI_PATH + "/{id}")
                        .build(id))
                .retrieve()
                .body(PokeAPISpecieResponseDTO.class);
        return flavorTextEntriesDTO.flavor_text_entries().stream().limit(1).toList();
    }

    public ChainEvolutionDTO getChainLinage(int id) {
        PokeApiLinageChainResponseDTO flavorTextEntriesDTO =
        restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(POKE_EVOLUTION_CHAIN_API_PATH + "/{id}")
                        .build(id))
                .retrieve()
                .body(PokeApiLinageChainResponseDTO.class);
        return flavorTextEntriesDTO.chain();
    }
}

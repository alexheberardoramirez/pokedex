package com.alex.pokedex.service;

import com.alex.pokedex.dto.PokeApiResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static com.alex.pokedex.util.PokemonConstants.POKEAPI_BASE_URL;
import static com.alex.pokedex.util.PokemonConstants.POKEAPI_PATH;

@Service
public class PokeApiService {

    private final RestClient restClient = RestClient.builder()
            .baseUrl(POKEAPI_BASE_URL)
            .build();

    public PokeApiResponseDto getPokemonsWithPagination(int offset, int limit) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(POKEAPI_PATH)
                        .queryParam("offset", offset)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .body(PokeApiResponseDto.class);
    }
}

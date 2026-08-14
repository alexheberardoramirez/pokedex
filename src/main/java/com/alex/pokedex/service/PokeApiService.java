package com.alex.pokedex.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PokeApiService {

    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://pokeapi.co/api/v2")
            .build();

    public String obtenerPokemonExternos(int offset, int limit) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/pokemon")
                        .queryParam("offset", offset)
                        .queryParam("limit", limit)
                        .build())
                .retrieve()
                .body(String.class);
    }
}

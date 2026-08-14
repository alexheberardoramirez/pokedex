package com.alex.pokedex.query;

import com.alex.pokedex.repository.PokemonRepository;
import com.alex.pokedex.service.PokeApiService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PokemonQueryService {

    private final PokemonRepository pokemonRepository;
    private final PokeApiService pokeApiService;

    public void getPokemonsWithPagination(int offset, int limit){
        String response = pokeApiService.obtenerPokemonExternos(offset, limit);
        System.out.println(response);

    }
}

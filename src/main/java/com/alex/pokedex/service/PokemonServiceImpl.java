package com.alex.pokedex.service;

import com.alex.pokedex.dto.PokemonResponseDTO;
import com.alex.pokedex.query.PokemonQueryService;
import com.alex.pokedex.repository.PokemonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PokemonServiceImpl implements PokemonService {

    private final PokemonQueryService pokemonQueryService;
    @Override
    public List<PokemonResponseDTO> getPokemonsWithPagination(int offset, int limit) {

        pokemonQueryService.getPokemonsWithPagination(offset, limit);
        return null;
    }
}

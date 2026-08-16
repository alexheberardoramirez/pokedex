package com.alex.pokedex.service;


import com.alex.pokedex.dto.PokemonRequestDTO;
import com.alex.pokedex.dto.PokemonRequestPatchDTO;
import com.alex.pokedex.dto.PokemonResponseDTO;
import com.alex.pokedex.dto.PokemonResponseDeleteDTO;
import com.alex.pokedex.model.Pokemon;

import java.util.List;

public interface PokemonService {
    List<PokemonResponseDTO> findAllPokemons();
    List<PokemonResponseDTO> getPokemonsWithPagination(int offset, int limit);

    PokemonResponseDTO getPokemonById(Long Id);
    PokemonResponseDTO createPokemon(PokemonRequestDTO pokemonRequestDTO);
    PokemonResponseDTO updatePokemon(int Id, PokemonRequestDTO pokemonRequestDTO);
    PokemonResponseDTO patchPokemon(int Id, PokemonRequestPatchDTO pokemonRequestPatchDTO);

    PokemonResponseDeleteDTO deletePokemon(int Id);

    Pokemon getPokemonFromDBOrAPIPagination(int Id);
}

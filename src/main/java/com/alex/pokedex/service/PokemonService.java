package com.alex.pokedex.service;


import com.alex.pokedex.dto.PokemonResponseDTO;

import java.util.List;

public interface PokemonService {

    List<PokemonResponseDTO> getPokemonsWithPagination(int offset, int limit);
}

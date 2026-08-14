package com.alex.pokedex.dto;

import java.util.List;

public record PokemonResponseDTO(
        Long id,
        Double mass,
        List<String> sprite,
        List<String> skills,
        List<String> statistics,
        String description,
        String lineage
) {

}

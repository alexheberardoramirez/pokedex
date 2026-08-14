package com.alex.pokedex.dto;

import java.util.List;

public record PokemonDetailsDto(
        int id,
        String name,
        int weight,
        SpritesDto sprites,
        List<PokemonAbilityDto> abilities,
        List<PokemonTypeDto> types
) {
}

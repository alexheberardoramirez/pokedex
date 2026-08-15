package com.alex.pokedex.dto;

import java.util.List;

public record PokemonDetailsDto(
        int id,
        String name,
        int weight,
        SpritesDto sprites,
        List<PokemonAbilityDto> abilities,
        List<PokemonTypeDto> types,
        List<StatsDTO> stats,
        PokeAPISpecieResponseDTO flavor_text_entries,
        PokeApiLinageChainResponseDTO chain
) {
}

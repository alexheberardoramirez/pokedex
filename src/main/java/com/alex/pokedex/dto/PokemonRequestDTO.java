package com.alex.pokedex.dto;

import com.alex.pokedex.model.PokemonAbility;
import com.alex.pokedex.model.PokemonSprites;
import com.alex.pokedex.model.PokemonType;
import com.alex.pokedex.model.Stats;

import java.util.List;

public record PokemonRequestDTO(
        String name,
        int weight,
        PokemonSprites sprites,
        List<PokemonAbility> abilities,
        List<PokemonType> types,
        List<Stats> stats,
        List<FlavorTextEntriesResponse> flavorTextEntries,
        ChainResponse chain
) {
}

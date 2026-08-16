package com.alex.pokedex.dto;

import com.alex.pokedex.model.PokemonAbility;
import com.alex.pokedex.model.PokemonSprites;
import com.alex.pokedex.model.PokemonType;
import com.alex.pokedex.model.Stats;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PokemonRequestDTO(
        @NotBlank(message = "Name is required and cannot be empty")
        String name,

        @NotNull(message = "Weight is required")
        int weight,

        @NotNull(message = "Sprites object is required")
        PokemonSprites sprites,

        @NotEmpty(message = "Abilities list cannot be empty")
        List<PokemonAbility> abilities,

        @NotEmpty(message = "Types list cannot be empty")
        List<PokemonType> types,

        @NotEmpty(message = "Stats list cannot be empty")
        List<Stats> stats,

        @NotEmpty(message = "Flavor text entries list cannot be empty")
        List<FlavorTextEntriesResponse> flavorTextEntries,

        @NotNull(message = "Chain response object is required")
        ChainResponse chain,

        String customName
) {
}

package com.alex.pokedex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record PokemonDescriptionDTO(
        @JsonProperty("flavor_text_entries")
        List<FlavorTextEntriesDTO> flavor_text_entries
) {
}

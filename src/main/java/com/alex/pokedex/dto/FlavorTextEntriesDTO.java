package com.alex.pokedex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FlavorTextEntriesDTO(
        @JsonProperty("flavor_text")
        String flavor_text
) {
}

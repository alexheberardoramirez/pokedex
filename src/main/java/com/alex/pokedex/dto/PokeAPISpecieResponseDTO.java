package com.alex.pokedex.dto;

import java.util.List;

public record PokeAPISpecieResponseDTO(
        List<FlavorTextEntriesDTO> flavor_text_entries
) {
}

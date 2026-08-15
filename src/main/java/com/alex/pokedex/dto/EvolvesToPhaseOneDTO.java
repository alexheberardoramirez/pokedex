package com.alex.pokedex.dto;

import java.util.List;

public record EvolvesToPhaseOneDTO(
        SpeciesDTO species,
        List<EvolvesToPhaseTwoDTO> evolves_to
) {
}

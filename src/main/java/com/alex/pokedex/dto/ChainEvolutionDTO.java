package com.alex.pokedex.dto;

import java.util.List;

public record ChainEvolutionDTO(
        SpeciesDTO species,
        List<EvolvesToPhaseOneDTO> evolves_to
) {
}

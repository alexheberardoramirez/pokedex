package com.alex.pokedex.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class PokemonSprites {
    private String backDefault;
    private String backShiny;
    private String frontDefault;
    private String frontShiny;
}


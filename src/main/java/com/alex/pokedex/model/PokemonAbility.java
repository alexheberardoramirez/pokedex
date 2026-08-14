package com.alex.pokedex.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class PokemonAbility {
    private String name;
}

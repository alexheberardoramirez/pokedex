package com.alex.pokedex.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class FlavorTextEntries {
    private  String flavor_text;
}

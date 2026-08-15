package com.alex.pokedex.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Chain {
    private String name_one;
    private String name_two;
    private String name_three;
}

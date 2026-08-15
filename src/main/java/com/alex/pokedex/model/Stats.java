package com.alex.pokedex.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Stats {
    private String base_stat;
    private String name;
}

package com.alex.pokedex.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Entity
@Table(name = "pokemons")
public class Pokemon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double mass;
    private List<String> sprite;
    private List<String> skills;
    private List<String> statistics;
    private String description;
    private String lineage;
}

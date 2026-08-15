package com.alex.pokedex.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pokemons")
public class Pokemon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int weight;
    @Embedded
    private PokemonSprites sprites;
    @ElementCollection
    @CollectionTable(name = "abilities", joinColumns = @JoinColumn(name = "pokemon_id"))
    private List<PokemonAbility> abilities;
    @ElementCollection
    @CollectionTable(name = "types", joinColumns = @JoinColumn(name = "pokemon_id"))
    private List<PokemonType> types;

    @ElementCollection
    @CollectionTable(name = "stats", joinColumns = @JoinColumn(name = "pokemon_id"))
    private List<Stats> stats;

    @ElementCollection
    @CollectionTable(name = "stats", joinColumns = @JoinColumn(name = "pokemon_id"))
    private List<Stats> flavorTextEntries;
}

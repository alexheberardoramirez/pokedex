package com.alex.pokedex.controller;

import com.alex.pokedex.dto.PokemonResponseDTO;
import com.alex.pokedex.service.PokemonService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pokemon")
@AllArgsConstructor
public class PokemonController {

    private final PokemonService pokemonService;
    @GetMapping
    public ResponseEntity<List<PokemonResponseDTO>> getPokemons(@RequestParam(defaultValue = "0") int offset,
                                                                @RequestParam(defaultValue = "20") int limit) {
        List<PokemonResponseDTO> pokemons = pokemonService.getPokemonsWithPagination(offset, limit);
        return ResponseEntity.ok(pokemons);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PokemonResponseDTO> getPokemonById() {
        return null;
    }
}

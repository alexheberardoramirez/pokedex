package com.alex.pokedex.controller;

import com.alex.pokedex.dto.PokemonResponseDTO;
import com.alex.pokedex.service.PokemonService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/pokemon")
@AllArgsConstructor
public class PokemonController {

    private final PokemonService pokemonService;
    @GetMapping
    public ResponseEntity<List<PokemonResponseDTO>> getPokemons(@RequestParam(defaultValue = "0") int offset,
                                                                @RequestParam(defaultValue = "20") int limit) {
        log.info("GET pokemons");
        List<PokemonResponseDTO> pokemons = pokemonService.getPokemonsWithPagination(offset, limit);
        log.info("GET Finalized ");
        return ResponseEntity.ok(pokemons);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PokemonResponseDTO> getPokemonById(@PathVariable Long id) {
        PokemonResponseDTO response = pokemonService.getPokemonById(id);
        return ResponseEntity.ok(response);
    }
}

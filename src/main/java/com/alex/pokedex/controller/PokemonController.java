package com.alex.pokedex.controller;

import com.alex.pokedex.dto.PokemonRequestDTO;
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

    @PostMapping
    public ResponseEntity<PokemonResponseDTO> postPokemon(@RequestBody PokemonRequestDTO pokemonRequest) {
        log.info("POST pokemon");
        PokemonResponseDTO response = pokemonService.createPokemon(pokemonRequest);
        log.info("POST pokemon finalized");
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PokemonResponseDTO>> getAllPokemon() {
        log.info("GET all pokemons");
        List<PokemonResponseDTO> pokemons = pokemonService.findAllPokemons();
        log.info("GET Finalized ");
        return ResponseEntity.ok(pokemons);
    }

    @GetMapping("/pagination")
    public ResponseEntity<List<PokemonResponseDTO>> getPokemonsWithPagination(@RequestParam(defaultValue = "0") int offset,
                                                                @RequestParam(defaultValue = "20") int limit) {
        log.info("GET pokemons with pagination");
        List<PokemonResponseDTO> pokemons = pokemonService.getPokemonsWithPagination(offset, limit);
        log.info("GET Finalized ");
        return ResponseEntity.ok(pokemons);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PokemonResponseDTO> getPokemonById(@PathVariable Long id) {
        log.info("GET pokemons by id");
        PokemonResponseDTO response = pokemonService.getPokemonById(id);
        log.info("GET pokemons by id finalized");
        return ResponseEntity.ok(response);
    }
}

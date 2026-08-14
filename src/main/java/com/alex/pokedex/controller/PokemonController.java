package com.alex.pokedex.controller;

import com.alex.pokedex.dto.PokemonResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pokemon")
@AllArgsConstructor
public class PokemonController {

    @GetMapping
    public ResponseEntity<PokemonResponseDTO> getPokemon() {
        return null;
    }
}

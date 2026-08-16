package com.alex.pokedex.validation;

import com.alex.pokedex.dto.PokemonRequestDTO;
import com.alex.pokedex.query.PokemonQueryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@AllArgsConstructor
public class PokemonValidator {
    private final PokemonQueryService pokemonQueryService;
    public void validateDuplicatePokemon(PokemonRequestDTO request) {
        if(pokemonQueryService.existByName(request.name())){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot create pokemon: The request name " + request.name() + " already exist in DB."
            );
        }
    }
    public void validatePokemonExist(int Id) {
        if(!pokemonQueryService.existsById(Id)){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot update pokemon: The pokemon entity with id: " + Id + " dosnt exist in DB."
            );
        }
    }

}

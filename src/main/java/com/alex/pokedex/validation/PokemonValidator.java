package com.alex.pokedex.validation;

import com.alex.pokedex.dto.PokemonRequestDTO;
import com.alex.pokedex.query.PokemonQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
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
                    HttpStatus.NOT_FOUND,
                    "The pokemon entity with id: " + Id + " dosnt exist in DB."
            );
        }
    }

    public void validatePokemonDeleted(int Id) {

        if(!pokemonQueryService.existsById(Id)){
            log.info("pokemon deleted successfully");
        } else {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Cannot delete pokemon: The pokemon entity with id: " + Id + " was not deleted from DB."
            );
        }
    }



}

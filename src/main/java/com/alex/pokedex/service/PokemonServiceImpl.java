package com.alex.pokedex.service;

import com.alex.pokedex.dto.PokemonRequestDTO;
import com.alex.pokedex.dto.PokemonRequestPatchDTO;
import com.alex.pokedex.dto.PokemonResponseDTO;
import com.alex.pokedex.mapper.PokemonMapper;
import com.alex.pokedex.model.Pokemon;
import com.alex.pokedex.query.PokemonQueryService;
import com.alex.pokedex.validation.PokemonValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class PokemonServiceImpl implements PokemonService {
    private final PokemonQueryService pokemonQueryService;
    private final PokemonMapper pokemonMapper;
    private final PokemonValidator pokemonValidator;

    @Override
    public PokemonResponseDTO createPokemon(PokemonRequestDTO pokemonRequestDTO) {
        pokemonValidator.validateDuplicatePokemon(pokemonRequestDTO);
        Pokemon pokemon = pokemonMapper.toEntitie(pokemonRequestDTO);
        Pokemon pokemonSaved = pokemonQueryService.save(pokemon);
        PokemonResponseDTO pokemonSavedMapped = pokemonMapper.toEntitie(pokemonSaved);
        return pokemonSavedMapped;
    }

    @Override
    public PokemonResponseDTO updatePokemon(int Id, PokemonRequestDTO pokemonRequestDTO) {
        pokemonValidator.validatePokemonExist(Id);
        Pokemon pokemonUpdated = pokemonMapper.toEntitieUpdate(pokemonRequestDTO, Long.valueOf(Id));
        Pokemon pokemonSaved =pokemonQueryService.save(pokemonUpdated);
        PokemonResponseDTO pokemonResponseDTO = pokemonMapper.toEntitie(pokemonSaved);
        return pokemonResponseDTO;
    }

    @Override
    public PokemonResponseDTO patchPokemon(int Id, PokemonRequestPatchDTO pokemonRequestDTO) {
        pokemonValidator.validatePokemonExist(Id);
        Pokemon pokemonFromDB = pokemonQueryService.findById(Id);
        Pokemon pokemonPatched = pokemonMapper.toEntitiePatch(pokemonFromDB, pokemonRequestDTO);
        Pokemon pokemonSaved =pokemonQueryService.save(pokemonPatched);
        PokemonResponseDTO pokemonResponseDTO = pokemonMapper.toEntitie(pokemonSaved);
        return pokemonResponseDTO;
    }

    @Override
    public boolean deletePokemon(int Id) {
        pokemonValidator.validatePokemonExist(Id);
        pokemonQueryService.delete(Id);
        pokemonValidator.validatePokemonDeleted(Id);
        return false;
    }

    @Override
    public Pokemon getPokemonFromDBOrAPIPagination(int Id) {
        Pokemon pokemon = null;
        if (pokemonQueryService.existsById(Id)) {
           pokemon = pokemonQueryService.findById(Id);
        } else {
            Pokemon pokemonFromApi = pokemonQueryService.findByIdInPokemonAPI(Id);
            pokemon = pokemonQueryService.save(pokemonFromApi);
        }
        return pokemon;
    }

    @Override
    public List<PokemonResponseDTO> findAllPokemons() {
        return pokemonMapper.toPokemonResponseDTO(pokemonQueryService.findAll());
    }

    @Override
    public List<PokemonResponseDTO> getPokemonsWithPagination(int offset, int limit) {
        List<Pokemon> collectPokemons = new ArrayList<>();
        for(int i = offset+1; i <= limit;i++){
            collectPokemons.add(getPokemonFromDBOrAPIPagination(i));
        }
        List<PokemonResponseDTO> pokemonSavedMapped = pokemonMapper.toPokemonResponseDTO(collectPokemons);
        return pokemonSavedMapped;
    }

    @Override
    public PokemonResponseDTO getPokemonById(Long Id) {
        pokemonValidator.validatePokemonExist(Id.intValue());
        Pokemon pokemon = pokemonQueryService.getPokemonById(Id);
        PokemonResponseDTO pokemonMapped = pokemonMapper.toEntitie(pokemon);
        return pokemonMapped;
    }


}

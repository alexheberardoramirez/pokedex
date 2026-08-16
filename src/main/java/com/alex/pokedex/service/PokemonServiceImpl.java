package com.alex.pokedex.service;

import com.alex.pokedex.dto.PokemonRequestDTO;
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
        Pokemon pokemonEntity = pokemonQueryService.findById(Id);
        System.out.println();
        return null;
    }

    @Override
    public List<PokemonResponseDTO> findAllPokemons() {
        return pokemonMapper.toPokemonResponseDTO(pokemonQueryService.findAll());
    }

    @Override
    public List<PokemonResponseDTO> getPokemonsWithPagination(int offset, int limit) {
        List<Pokemon> collectPokemons = new ArrayList<>();
        for(int i = offset+1; i <= limit;i++){
            if (pokemonQueryService.existsById(i)){
                collectPokemons.add(pokemonQueryService.findById(i));
            }else{
                Pokemon pokemonFromApi = pokemonQueryService.findByIdInPokemonAPI(i);
                collectPokemons.add(pokemonQueryService.save(pokemonFromApi));
            }
        }
        List<PokemonResponseDTO> pokemonSavedMapped = pokemonMapper.toPokemonResponseDTO(collectPokemons);
        return pokemonSavedMapped;
    }

    @Override
    public PokemonResponseDTO getPokemonById(Long Id) {
        Pokemon pokemon = pokemonQueryService.getPokemonById(Id);
        PokemonResponseDTO pokemonMapped = pokemonMapper.toEntitie(pokemon);
        return pokemonMapped;
    }


}

package com.alex.pokedex.service;

import com.alex.pokedex.dto.PokemonResponseDTO;
import com.alex.pokedex.mapper.PokemonMapper;
import com.alex.pokedex.model.Pokemon;
import com.alex.pokedex.query.PokemonQueryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class PokemonServiceImpl implements PokemonService {
    private final PokemonQueryService pokemonQueryService;

    private final PokemonMapper pokemonMapper;

    @Override
    public List<PokemonResponseDTO> findAllPokemons() {
        return pokemonMapper.toPokemonResponseDTO(pokemonQueryService.findAll());
    }

    @Override
    public List<PokemonResponseDTO> getPokemonsWithPagination(int offset, int limit) {
        List<Pokemon> resultFromPokemonAPI = pokemonQueryService.getPokemonsWithPagination(offset, limit);
        pokemonMapper.removeId(resultFromPokemonAPI);
        log.info("Saving pokemons in DB");
        List<Pokemon> pokemonEntitiesSaved = pokemonQueryService.saveAll(resultFromPokemonAPI);
        log.info("Saved successfully");
        List<PokemonResponseDTO> pokemonSavedMapped = pokemonMapper.toPokemonResponseDTO(pokemonEntitiesSaved);
        return pokemonSavedMapped;
    }

    @Override
    public PokemonResponseDTO getPokemonById(Long Id) {
        Pokemon pokemon = pokemonQueryService.getPokemonById(Id);
        PokemonResponseDTO pokemonMapped = pokemonMapper.toEntitie(pokemon);
        return pokemonMapped;
    }
}

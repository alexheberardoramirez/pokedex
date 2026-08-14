package com.alex.pokedex.service;

import com.alex.pokedex.dto.PokemonDetailsDto;
import com.alex.pokedex.dto.PokemonResponseDTO;
import com.alex.pokedex.mapper.PokemonMapper;
import com.alex.pokedex.model.Pokemon;
import com.alex.pokedex.query.PokemonQueryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PokemonServiceImpl implements PokemonService {
    private final PokemonQueryService pokemonQueryService;

    private final PokemonMapper pokemonMapper;

    @Override
    public List<PokemonResponseDTO> getPokemonsWithPagination(int offset, int limit) {
        List<PokemonDetailsDto> resultFromPokemonAPI = pokemonQueryService.getPokemonsWithPagination(offset, limit);
        List<Pokemon> pokemonEntities =pokemonMapper.toEntities(resultFromPokemonAPI);
        List<Pokemon> pokemonEntitiesSaved = pokemonQueryService.saveAll(pokemonEntities);
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

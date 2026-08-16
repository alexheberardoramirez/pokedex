package com.alex.pokedex.query;

import com.alex.pokedex.dto.*;
import com.alex.pokedex.mapper.PokemonMapper;
import com.alex.pokedex.model.Chain;
import com.alex.pokedex.model.FlavorTextEntries;
import com.alex.pokedex.model.Pokemon;
import com.alex.pokedex.repository.PokemonRepository;
import com.alex.pokedex.service.PokeApiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class PokemonQueryService {

    private final PokemonRepository pokemonRepository;
    private final PokeApiService pokeApiService;

    private final PokemonMapper pokemonMapper;

    public Pokemon findByIdInPokemonAPI(int Id) {
        log.info("Lookup for pokemon on pokemon api with id: "+ Id);
        PokemonDetailsDto pokemonDetailsDto = pokeApiService.getPokemonById(Id);
        Pokemon pokemonFirstSection = pokemonMapper.dtoToEntity(pokemonDetailsDto);

        List<FlavorTextEntriesDTO> pokemonDescription = getNarrativeDescriptionPokemonAPI(Id);
        List<FlavorTextEntries> flavorTextEntries =
                pokemonMapper.fromToFlavorTextEntriesDTOsToFlavorTextEntries(pokemonDescription);

        Pokemon pokemonWithDescription =pokemonMapper.setFlavorTextEntries(flavorTextEntries, pokemonFirstSection);

        ChainEvolutionDTO chainEvolutionDTO = getChainLinage(Id);
        Chain chainLinage = pokemonMapper.toChainEntity(chainEvolutionDTO);
        Pokemon pokemonFullWithLinage = pokemonMapper.setChainLinage(chainLinage,pokemonWithDescription);
        log.info("pokemon retrieved successfully");
        return pokemonFullWithLinage;
    }

    public List<FlavorTextEntriesDTO> getNarrativeDescriptionPokemonAPI(int Id) {
        List<FlavorTextEntriesDTO> flavorTextEntriesDTOList = pokeApiService.getNarrativeDescription(Id);
        return flavorTextEntriesDTOList;
    }

    public ChainEvolutionDTO getChainLinage(int Id) {
        ChainEvolutionDTO chainEvolutionDTO = pokeApiService.getChainLinage(Id);
        return chainEvolutionDTO;
    }

    public Pokemon findById(int Id) {
        log.info("Lookup for pokemon in db with id: "+Id);
        Pokemon pokemon = pokemonRepository.findById(Long.valueOf(Id)).orElseThrow();
        log.info("pokemon retrieved successfully");
        return pokemon;
    }

    public void delete(int Id) {
        log.info("Deleting pokemon in db with id: "+Id);
        pokemonRepository.deleteById(Long.valueOf(Id));
    }

    public boolean existByName(String name) {
        return pokemonRepository.existsByName(name);
    }
    public Pokemon save(Pokemon pokemon) {
        log.info("Inserting pokemon "+pokemon.getName()+" in DB");
        Pokemon pokemonSaved = pokemonRepository.save(pokemon);
        log.info("Pokemon "+pokemon.getName()+" Saved");
           return pokemonSaved;
    }
    public List<Pokemon> saveAll(List<Pokemon> pokemons) {
        return pokemonRepository.saveAll(pokemons);
    }

    public List<Pokemon> findAll() {
        log.info("Query all pokemons in DB");
        List<Pokemon> pokemons = pokemonRepository.findAll();
        log.info("Query retreive all pokemons successfully");
        return pokemons;
    }

    public boolean existsById(int Id) {
        log.info("Query DB");
        boolean exist = pokemonRepository.existsById(Long.valueOf(Id));
        log.info("Query DB successfully");
        return exist;
    }

    public Pokemon getPokemonById(Long Id) {
        log.info("Query to get pokemon by id");
        Pokemon pokemon = pokemonRepository.findById(Id).orElseThrow();
        log.info("Query DB successfully");
        return pokemon;
    }

}

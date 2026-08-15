package com.alex.pokedex.mapper;

import com.alex.pokedex.dto.*;
import com.alex.pokedex.model.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PokemonMapper {

    PokemonResponseDTO toEntitie(Pokemon pokemon);
    List<PokemonResponseDTO> toPokemonResponseDTO(List<Pokemon> pokemonEntities);

    List<Pokemon> toEntities(List<PokemonDetailsDto> pokemonDetailsDto);

    @Mapping(target = "name", source = "name")
    @Mapping(target = "abilities", source = "abilities")
    @Mapping(target = "types", source = "types")
    @Mapping(target = "stats", source = "stats")
    Pokemon dtoToEntity(PokemonDetailsDto dto);



    @Mapping(target = "name", source = "ability.name")
    PokemonAbility toAbilityEntity(PokemonAbilityDto dto);

    @Mapping(target = "name", source = "type.name")
    PokemonType toTypeEntity(PokemonTypeDto dto);

    @Mapping(target = "base_stat", source = "base_stat")
    @Mapping(target = "name", source = "stat.name")
    Stats toStatEntity(StatsDTO dto);

    //List<Stats>  PokeAPISpecieResponseDTO

    List<FlavorTextEntries> toStatsEntity(List<FlavorTextEntriesDTO> flavorTextEntriesDTOList);

    @Mapping(target = "name_one", source = "species.name")
    @Mapping(target = "name_two", expression = "java(getSecondEvolutionName(chainEvolutionDTO))")
    @Mapping(target = "name_three", expression = "java(getThirdEvolutionName(chainEvolutionDTO))")
    Chain toChainEntity(ChainEvolutionDTO chainEvolutionDTO);


    default String getSecondEvolutionName(ChainEvolutionDTO dto) {
        if (dto == null || dto.evolves_to() == null) return null;

        return dto.evolves_to().stream()
                .limit(1)
                .map(evolves -> evolves.species() != null ? evolves.species().name() : null)
                .findFirst()
                .orElse(null);
    }

    default String getThirdEvolutionName(ChainEvolutionDTO dto) {
        if (dto == null || dto.evolves_to() == null) return null;

        return dto.evolves_to().stream()
                .map(e->e.evolves_to().stream()
                        .map(ev->ev.species() != null ? ev.species().name() : null)
                        .findFirst()
                        .orElse(null)
                ).findFirst().orElse(null);


    }

   // @AfterMapping
   default void removeId(@MappingTarget List<Pokemon> pokemons) {
       if (pokemons != null) {
           pokemons.forEach(pokemon -> {
               if (pokemon != null) {
                   pokemon.setId(null); // Borra el ID y lo vuelve NULL de forma segura en cada uno
               }
           });
       }
   }

}

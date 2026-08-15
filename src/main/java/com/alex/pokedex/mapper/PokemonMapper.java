package com.alex.pokedex.mapper;

import com.alex.pokedex.dto.*;
import com.alex.pokedex.model.Pokemon;
import com.alex.pokedex.model.PokemonAbility;
import com.alex.pokedex.model.PokemonType;
import com.alex.pokedex.model.Stats;
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

    @AfterMapping
    default void forzarIdNulo(@MappingTarget Pokemon pokemon) {
        if (pokemon != null) {
            pokemon.setId(null); // Borra el ID y lo vuelve NULL de forma segura
        }
    }
}

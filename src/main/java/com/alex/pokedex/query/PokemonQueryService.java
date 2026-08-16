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
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@AllArgsConstructor
public class PokemonQueryService {

    private final PokemonRepository pokemonRepository;
    private final PokeApiService pokeApiService;

    private final PokemonMapper pokemonMapper;


    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Pokemon findByIdInPokemonAPI(int Id) {
        log.info("Query find by id in pokemon api");
        PokemonDetailsDto pokemonDetailsDto = pokeApiService.getPokemonById(Id);
        Pokemon pokemonFirstSection = pokemonMapper.dtoToEntity(pokemonDetailsDto);

        List<FlavorTextEntriesDTO> pokemonDescription = getNarrativeDescriptionPokemonAPI(Id);
        List<FlavorTextEntries> flavorTextEntries =
                pokemonMapper.fromToFlavorTextEntriesDTOsToFlavorTextEntries(pokemonDescription);

        Pokemon pokemonWithDescription =pokemonMapper.setFlavorTextEntries(flavorTextEntries, pokemonFirstSection);

        ChainEvolutionDTO chainEvolutionDTO = getChainLinage(Id);
        Chain chainLinage = pokemonMapper.toChainEntity(chainEvolutionDTO);
        Pokemon pokemonFullWithLinage = pokemonMapper.setChainLinage(chainLinage,pokemonWithDescription);
        log.info("Successfully");
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
        log.info("Query DB by id");
        Pokemon pokemon = pokemonRepository.findById(Long.valueOf(Id)).orElseThrow();
        log.info("Query DB successfully");
        return pokemon;
    }

    public boolean existByName(String name) {
        return pokemonRepository.existsByName(name);
    }
    public Pokemon save(Pokemon pokemon) {
        return pokemonRepository.save(pokemon);
    }
    public List<Pokemon> saveAll(List<Pokemon> pokemons) {
        return pokemonRepository.saveAll(pokemons);
    }

    public List<Pokemon> findAll() {
        log.info("Query DB");
        List<Pokemon> pokemons = pokemonRepository.findAll();
        log.info("Query DB successfully");
        return pokemons;
    }

    public boolean existsById(int Id) {
        log.info("Query DB");
        boolean exist = pokemonRepository.existsById(Long.valueOf(Id));
        log.info("Query DB successfully");
        return exist;
    }

    public Pokemon getPokemonById(Long Id) {
        log.info("Query DB");
        Pokemon pokemon = pokemonRepository.findById(Id).orElseThrow();
        log.info("Query DB successfully");
        return pokemon;
    }

    public List<Pokemon>getPokemonsWithPagination(int offset, int limit){
        log.info("Calling pokemon API");
        PokeApiResponseDto response = pokeApiService.getPokemonsWithPagination(offset, limit);
        log.info("Get response successfully");
        log.info("Calling pokemon API to get pokemon Details");
        List<PokemonDetailsDto> getPokemonDetails = getPokemonDetailsInParallel(response.results());
        log.info("Get pokemon details response successfully");
        List<Pokemon> pokemonsMapped = pokemonMapper.toEntities(getPokemonDetails);
        List<Pokemon> pokemonsWithDescription =
        pokemonsMapped.stream()
                .map(e-> {
                    log.info("Calling pokemon API to get Linage");
                    e.setChain(
                            pokemonMapper.toChainEntity(pokeApiService.getChainLinage(e.getId().intValue()))
                    );
                    log.info("Get pokemon linage response successfully");
                    log.info("Calling pokemon API to get Description");
                    e.setFlavorTextEntries(
                        pokemonMapper.fromToFlavorTextEntriesDTOsToFlavorTextEntries(pokeApiService.getNarrativeDescription(e.getId().intValue()))

                    );
                    log.info("Get pokemon description response successfully");
                    return e;
                })
                .toList();


        return pokemonsWithDescription;
    }

    public List<PokemonDetailsDto> getPokemonDetailsInParallel(List<PokemonResultDto> listPokemon) {

        List<CompletableFuture<PokemonDetailsDto>> futures = listPokemon.stream()
                .map(pokemon -> CompletableFuture.supplyAsync(() -> {
                    try {
                        HttpRequest request = HttpRequest.newBuilder()
                                .uri(URI.create(pokemon.url()))
                                .GET()
                                .build();

                        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                        return objectMapper.readValue(response.body(), PokemonDetailsDto.class);

                    } catch (Exception e) {
                        log.error("Error to get Pokemon details of " + pokemon.name() + ": " + e.getMessage());
                        return null;
                    }
                }))
                .toList();

        CompletableFuture<Void> todasLasPeticiones = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        return todasLasPeticiones.thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .filter(pokemon -> pokemon != null)
                        .toList())
                .join();
    }

}

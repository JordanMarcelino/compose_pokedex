package com.example.pokemonapp.data.repository.datasource

import com.example.pokemonapp.data.model.pokemon_list.PokemonList
import com.example.pokemonapp.data.model.single_pokemon.Pokemon
import retrofit2.Response

interface PokemonRemoteDataSource {

    suspend fun getPokemonList() : Response<PokemonList>
    suspend fun getPokemonDetails() : Response<Pokemon>

}
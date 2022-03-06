package com.example.pokemonapp.data.repository.datasourceimpl

import com.example.pokemonapp.data.model.pokemon_list.PokemonList
import com.example.pokemonapp.data.model.single_pokemon.Pokemon
import com.example.pokemonapp.data.repository.datasource.PokemonRemoteDataSource
import retrofit2.Response

class PokemonRemoteDataSourceImpl(

): PokemonRemoteDataSource {
    override suspend fun getPokemonList(): Response<PokemonList> {
        TODO("Not yet implemented")
    }

    override suspend fun getPokemonDetails(): Response<Pokemon> {
        TODO("Not yet implemented")
    }
}
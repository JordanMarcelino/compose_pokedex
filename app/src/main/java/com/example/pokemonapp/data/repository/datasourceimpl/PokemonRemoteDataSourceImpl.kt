package com.example.pokemonapp.data.repository.datasourceimpl

import com.example.pokemonapp.data.api.PokemonApi
import com.example.pokemonapp.data.model.pokemon_list.PokemonList
import com.example.pokemonapp.data.model.single_pokemon.Pokemon
import com.example.pokemonapp.data.repository.datasource.PokemonRemoteDataSource
import com.example.pokemonapp.data.util.Resource
import retrofit2.Response

class PokemonRemoteDataSourceImpl(
    private val pokemonApi: PokemonApi
): PokemonRemoteDataSource {

    override suspend fun getPokemonList(offset : Int, limit : Int): Response<PokemonList> = pokemonApi.getPokemonList(offset, limit)

    override suspend fun getPokemonDetails(name : String): Response<Pokemon> = pokemonApi.getPokemonDetails(name)

}
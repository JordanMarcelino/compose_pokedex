package com.example.pokemonapp.data.repository

import com.example.pokemonapp.data.model.pokemon_list.PokemonList
import com.example.pokemonapp.data.model.single_pokemon.Pokemon
import com.example.pokemonapp.data.repository.datasource.PokemonRemoteDataSource
import com.example.pokemonapp.data.util.Resource
import com.example.pokemonapp.domain.repository.PokemonRepository
import retrofit2.Response

class PokemonRepositoryImpl(
    private val remoteDataSource: PokemonRemoteDataSource
) : PokemonRepository {

    override suspend fun getPokemonList(offset : Int, limit : Int): Resource<PokemonList> = pokemonListResponseToResource(remoteDataSource.getPokemonList(offset, limit))

    override suspend fun getPokemonDetails(name : String): Resource<Pokemon> = pokemonResponseToResource(remoteDataSource.getPokemonDetails(name))

    private fun pokemonListResponseToResource(response : Response<PokemonList>) : Resource<PokemonList>{
        try {
            if (response.isSuccessful){
                response.body()?.let {
                    return Resource.Success(it)
                }
            } else {
                return Resource.Error(response.message())
            }
        }catch (e : Exception){
            return Resource.Error(e.message.toString())
        }
        return Resource.Error("Error converting response")
    }

    private fun pokemonResponseToResource(response : Response<Pokemon>) : Resource<Pokemon>{
        try {
            if (response.isSuccessful){
                response.body()?.let {
                    return Resource.Success(it)
                }
            } else {
                return Resource.Error(response.message())
            }
        }catch (e : Exception){
            return Resource.Error(e.message.toString())
        }
        return Resource.Error("Error converting response")
    }
}
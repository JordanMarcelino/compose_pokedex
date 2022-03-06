package com.example.pokemonapp.data.api

import com.example.pokemonapp.data.model.pokemon_list.PokemonList
import com.example.pokemonapp.data.model.single_pokemon.Pokemon
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") offset : Int,
        @Query("limit") limit : Int,
    ) : Response<PokemonList>

    @GET("pokemon/{name}")
    suspend fun getPokemonDetails(
        @Path("name") name : String
    ) : Response<Pokemon>

}
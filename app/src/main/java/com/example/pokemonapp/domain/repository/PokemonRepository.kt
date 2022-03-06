package com.example.pokemonapp.domain.repository

import com.example.pokemonapp.data.model.pokemon_list.PokemonList
import com.example.pokemonapp.data.model.single_pokemon.Pokemon
import com.example.pokemonapp.data.util.Resource

interface PokemonRepository {
    suspend fun getPokemonList(offset : Int, limit : Int) : Resource<PokemonList>
    suspend fun getPokemonDetails(name : String) : Resource<Pokemon>
}
package com.example.pokemonapp.domain.usecase

import com.example.pokemonapp.domain.repository.PokemonRepository

class GetPokemonUseCase(private val pokemonRepository: PokemonRepository) {
    suspend fun execute(name : String) = pokemonRepository.getPokemonDetails(name)
}
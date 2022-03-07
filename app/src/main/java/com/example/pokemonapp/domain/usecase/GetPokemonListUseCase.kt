package com.example.pokemonapp.domain.usecase

import com.example.pokemonapp.domain.repository.PokemonRepository

class GetPokemonListUseCase(private val pokemonRepository: PokemonRepository) {
    suspend fun execute(offset: Int, limit : Int) = pokemonRepository.getPokemonList(offset, limit)
}
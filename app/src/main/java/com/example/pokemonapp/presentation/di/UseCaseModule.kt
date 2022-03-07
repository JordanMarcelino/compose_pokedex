package com.example.pokemonapp.presentation.di

import com.example.pokemonapp.domain.repository.PokemonRepository
import com.example.pokemonapp.domain.usecase.GetPokemonListUseCase
import com.example.pokemonapp.domain.usecase.GetPokemonUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun providesGetPokemonListUseCase(pokemonRepository: PokemonRepository) : GetPokemonListUseCase = GetPokemonListUseCase(pokemonRepository)

    @Singleton
    @Provides
    fun providesGetPokemonUseCase(pokemonRepository: PokemonRepository) : GetPokemonUseCase = GetPokemonUseCase(pokemonRepository)
}
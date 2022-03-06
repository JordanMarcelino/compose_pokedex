package com.example.pokemonapp.presentation.di

import com.example.pokemonapp.data.repository.PokemonRepositoryImpl
import com.example.pokemonapp.data.repository.datasource.PokemonRemoteDataSource
import com.example.pokemonapp.domain.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun providesPokemonRepository(remoteDataSource: PokemonRemoteDataSource) : PokemonRepository = PokemonRepositoryImpl(remoteDataSource)

}
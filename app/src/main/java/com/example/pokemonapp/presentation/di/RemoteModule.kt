package com.example.pokemonapp.presentation.di

import com.example.pokemonapp.data.api.PokemonApi
import com.example.pokemonapp.data.repository.datasource.PokemonRemoteDataSource
import com.example.pokemonapp.data.repository.datasourceimpl.PokemonRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Singleton
    @Provides
    fun providesPokemonRemoteDataSource(pokemonApi: PokemonApi) : PokemonRemoteDataSource = PokemonRemoteDataSourceImpl(pokemonApi)

}
package com.example.pokemonapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pokemonapp.domain.usecase.GetPokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val getPokemonUseCase: GetPokemonUseCase
) : ViewModel(){


    suspend fun getPokemonDetail(name : String) = getPokemonUseCase.execute(name.lowercase())


}
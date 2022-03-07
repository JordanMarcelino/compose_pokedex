package com.example.pokemonapp.presentation.viewmodel

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.example.pokemonapp.data.model.PokemonEntryList
import com.example.pokemonapp.data.util.Const.PAGE_SIZE
import com.example.pokemonapp.data.util.Resource
import com.example.pokemonapp.domain.usecase.GetPokemonListUseCase
import com.example.pokemonapp.domain.usecase.GetPokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val getPokemonUseCase: GetPokemonUseCase
) : ViewModel() {

    private var currentPage = 0
    var pokemonList = mutableStateOf<List<PokemonEntryList>>(listOf())
    var currentState = mutableStateOf<Resource<Any>>(Resource.Loading())
    var endReached = mutableStateOf(false)


    init {
        loadPokemonPaginated()
    }

    fun loadPokemonPaginated() = viewModelScope.launch {
        when (val response = getPokemonListUseCase.execute(currentPage * PAGE_SIZE, PAGE_SIZE)) {
            is Resource.Success -> {
                endReached.value = currentPage * PAGE_SIZE >= response.data?.count!!
                val pokedexEntries = response.data.results!!.mapIndexed { _, result ->
                    val number = if (result.url!!.endsWith("/")) {
                        result.url.dropLast(1).takeLastWhile { it.isDigit() }
                    } else {
                        result.url.takeLastWhile { it.isDigit() }
                    }

                    val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"

                    PokemonEntryList(result.name!!.uppercase(), number.toInt(), url)
                }
                currentPage++
                currentState.value = Resource.Success("Success")
                pokemonList.value += pokedexEntries
            }
            is Resource.Error -> {
                currentState.value = Resource.Error("Error loading page")
            }
            else -> { currentState.value = Resource.Error("No Internet Connection") }
        }
    }

    fun calcDominateColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bmp).generate {
            it?.dominantSwatch?.rgb?.let { color ->
                onFinish(Color(color))
            }
        }
    }
}
package com.example.pokemonapp.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import com.example.pokemonapp.PokemonApplication
import com.example.pokemonapp.data.model.single_pokemon.Pokemon
import com.example.pokemonapp.data.util.Resource
import com.example.pokemonapp.domain.usecase.GetPokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val app: Application,
    private val getPokemonUseCase: GetPokemonUseCase
) : AndroidViewModel(app) {


     suspend fun getPokemonDetail(name: String) : Resource<Pokemon> {
        return try {
            if (isNetworkAvailable()) {
                getPokemonUseCase.execute(name.lowercase())
            }else {
                Resource.Error("No Network Available")
            }
        }catch (e : Exception) {
            Resource.Error(e.message.toString())
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            (getApplication<PokemonApplication>().getSystemService(Context.CONNECTIVITY_SERVICE)) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}

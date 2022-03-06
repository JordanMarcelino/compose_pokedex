package com.example.pokemonapp.data.model.single_pokemon


import com.google.gson.annotations.SerializedName

data class GenerationV(
    @SerializedName("black-white")
    val blackWhite: BlackWhite?
)
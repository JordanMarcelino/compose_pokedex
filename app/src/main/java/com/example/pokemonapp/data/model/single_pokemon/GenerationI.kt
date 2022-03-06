package com.example.pokemonapp.data.model.single_pokemon


import com.google.gson.annotations.SerializedName

data class GenerationI(
    @SerializedName("red-blue")
    val redBlue: RedBlue?,
    @SerializedName("yellow")
    val yellow: Yellow?
)
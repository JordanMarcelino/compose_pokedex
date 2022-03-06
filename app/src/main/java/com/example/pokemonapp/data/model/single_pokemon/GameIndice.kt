package com.example.pokemonapp.data.model.single_pokemon


import com.google.gson.annotations.SerializedName

data class GameIndice(
    @SerializedName("game_index")
    val gameIndex: Int?,
    @SerializedName("version")
    val version: Version?
)
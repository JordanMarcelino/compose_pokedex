package com.example.pokemonapp.data.model.single_pokemon


import com.google.gson.annotations.SerializedName

data class OfficialArtwork(
    @SerializedName("front_default")
    val frontDefault: String?
)
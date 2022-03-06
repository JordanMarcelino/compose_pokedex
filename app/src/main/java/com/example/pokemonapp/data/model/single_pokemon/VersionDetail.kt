package com.example.pokemonapp.data.model.single_pokemon


import com.google.gson.annotations.SerializedName

data class VersionDetail(
    @SerializedName("rarity")
    val rarity: Int?,
    @SerializedName("version")
    val version: VersionX?
)
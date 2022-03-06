package com.example.pokemonapp.data.model.single_pokemon


import com.google.gson.annotations.SerializedName

data class VersionGroup(
    @SerializedName("name")
    val name: String?,
    @SerializedName("url")
    val url: String?
)
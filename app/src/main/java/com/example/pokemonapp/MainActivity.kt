package com.example.pokemonapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokemonapp.presentation.compose.PokemonDetailsScreen
import com.example.pokemonapp.presentation.compose.PokemonListScreen
import com.example.pokemonapp.ui.theme.PokemonAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonAppTheme {
                val controller = rememberNavController()

                NavHost(navController = controller, startDestination = "pokemon_list_screen") {
                    composable(route = "pokemon_list_screen") {
                        PokemonListScreen(navController = controller)
                    }
                    composable(route = "pokemon_details_screen/{dominantColor}/{pokemonName}",
                        arguments = listOf(
                            navArgument(name = "dominantColor") {
                                type = NavType.IntType
                            },
                            navArgument(name = "pokemonName") {
                                type = NavType.StringType
                            }
                        )) {
                        val dominantColor = remember {
                            val color = it.arguments?.getInt("dominantColor")
                            color?.let { Color(it) } ?: Color.White
                        }
                        val pokemonName = remember {
                            it.arguments?.getString("pokemonName")
                        }
                        PokemonDetailsScreen(
                            navController = controller,
                            pokemonName = pokemonName!!,
                            dominantColor = dominantColor)
                    }
                }
            }
        }
    }
}

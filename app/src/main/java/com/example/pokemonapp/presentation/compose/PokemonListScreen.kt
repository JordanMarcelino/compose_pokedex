package com.example.pokemonapp.presentation.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transition.Transition
import com.example.pokemonapp.R
import com.example.pokemonapp.data.model.PokemonEntryList
import com.example.pokemonapp.presentation.viewmodel.PokemonListViewModel
import com.example.pokemonapp.ui.theme.RobotoCondensed

@Composable
fun PokemonListScreen(
    navController: NavController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                contentDescription = "Pokemon Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding()
                    .align(CenterHorizontally)
            )
            SearchBar(
                hint = "Search",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onSearch = {

                }
            )
        }
    }
}

@Composable
fun PokemonEntry(
    entry: PokemonEntryList,
    modifier: Modifier = Modifier,
    navController: NavController,
    listViewModel: PokemonListViewModel = hiltViewModel()
) {
    val defaultDominantColor = MaterialTheme.colors.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(
        modifier = modifier
            .shadow(10.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(16.dp))
            .aspectRatio(1f)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        dominantColor,
                        defaultDominantColor
                    )
                )
            )
            .clickable {
                navController.navigate(
                    route = "pokemon_details_screen/${dominantColor.toArgb()}/${entry.name}"
                )
            },
        contentAlignment = Center,
    ) {
        Column {
            AsyncImage(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(entry.imageURL)
                    .target {
                        listViewModel.calcDominateColor(it) { color ->
                            dominantColor = color
                        }
                    }
                    .crossfade(true)
                    .build(),
                contentDescription = entry.name,
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally)
            )
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = Modifier.scale(0.5f)
            )
            Text(
                text = entry.name,
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun PokemonRow(
    entries: List<PokemonEntryList>,
    navController: NavController,
    rowIndex: Int
) {
    Column {
        Row {
            PokemonEntry(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (entries.size >= rowIndex * 2 + 2) {
                PokemonEntry(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {},
    hint: String = ""
) {
    var text by remember {
        mutableStateOf("")
    }

    Box(modifier = modifier) {
        TextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 18.dp)
                .shadow(4.dp, RoundedCornerShape(26.dp))
                .clip(shape = AbsoluteRoundedCornerShape(20.dp)),
            placeholder = {
                Text(text = hint)
            },
            textStyle = TextStyle(fontSize = 18.sp),
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                textColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
    }
}

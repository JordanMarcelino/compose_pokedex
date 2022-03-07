package com.example.pokemonapp.presentation.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.pokemonapp.R
import com.example.pokemonapp.data.model.PokemonEntryList
import com.example.pokemonapp.data.util.Resource
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
            Spacer(modifier = Modifier.height(16.dp))
            PokemonList(navController = navController)
        }
    }
}

@Composable
fun PokemonList(
    navController: NavController,
    listViewModel: PokemonListViewModel = hiltViewModel()
) {
    val pokemonList by remember { listViewModel.pokemonList }
    val currentState by remember { listViewModel.currentState }
    val endReached by remember { listViewModel.endReached }

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        val itemCount = if (pokemonList.size % 2 == 0) {
            pokemonList.size / 2
        } else {
            pokemonList.size / 2 + 1
        }
        items(count = itemCount) {
            if (it >= itemCount - 1 && !endReached && (currentState !is Resource.Loading)) {
                listViewModel.loadPokemonPaginated()
            }
            PokemonRow(entries = pokemonList, navController = navController, rowIndex = it)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Center
    ) {
        if (currentState is Resource.Loading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if (currentState is Resource.Error) {
            Retry(currentState.message.toString()) {
                listViewModel.loadPokemonPaginated()
            }
        }
    }

}

@Composable
fun Retry(
    error: String,
    onRetry: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = error, fontSize = 24.sp, color = Color.Red, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
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

            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(entry.imageURL)
                    .crossfade(true)
                    .build(),
                contentDescription = entry.name,
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally),
                onSuccess = {
                    listViewModel.calcDominateColor(it.result.drawable){ color ->
                        dominantColor = color
                    }
                },
                loading = {
                    CircularProgressIndicator(color = MaterialTheme.colors.primary, modifier = Modifier.scale(0.5f))
                }
            )
            Text(
                text = entry.name,
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
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
            Spacer(modifier = Modifier.width(24.dp))
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

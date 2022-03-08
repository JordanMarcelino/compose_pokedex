package com.example.pokemonapp.presentation.compose

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.pokemonapp.R
import com.example.pokemonapp.data.model.single_pokemon.Pokemon
import com.example.pokemonapp.data.model.single_pokemon.Type
import com.example.pokemonapp.data.util.PokemonParse
import com.example.pokemonapp.data.util.Resource
import com.example.pokemonapp.presentation.viewmodel.PokemonDetailsViewModel
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun PokemonDetailsScreen(
    navController: NavController,
    detailsViewModel: PokemonDetailsViewModel = hiltViewModel(),
    pokemonName: String,
    dominantColor: Color
) {

    val pokemon = produceState<Resource<Pokemon>>(initialValue = Resource.Loading()) {
        value = detailsViewModel.getPokemonDetail(pokemonName)
    }

    val scope = rememberCoroutineScope()

    if (pokemon.value is Resource.Success) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(dominantColor)
        ) {
            TopSection(navController = navController)
            DetailsPokemon(
                pokemon = pokemon.value,
                pokemonName = pokemonName,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 20.dp + (200.dp / 2f),
                        bottom = 16.dp,
                        end = 16.dp,
                        start = 16.dp
                    )
                    .shadow(10.dp, RoundedCornerShape(20.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colors.surface)
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                loadingModifier = Modifier
                    .size(100.dp)
                    .align(Alignment.Center)
                    .padding(
                        top = 20.dp + 200.dp / 2f,
                        bottom = 16.dp,
                        end = 16.dp,
                        start = 16.dp
                    )
            )
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                PokemonImage(
                    pokemon = pokemon.value,
                    pokemonName = pokemonName
                )
            }
        }
    } else if (pokemon.value is Resource.Loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(dominantColor)
        ) {
            TopSection(navController = navController)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                )
            }
        }
    } else if (pokemon.value is Resource.Error) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(dominantColor)
        ) {
            TopSection(navController = navController)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Retry(error = pokemon.value.message!!) {
                    scope.launch {
                        detailsViewModel.getPokemonDetail(pokemonName)
                    }
                }
            }
        }
    }
}


@Composable
fun PokemonDetailsSection(
    pokemon: Pokemon,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "#${pokemon.id} ${
                pokemon.name!!.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
            }",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.height(10.dp))
        PokemonTypeSection(types = pokemon.types!!)
        Spacer(modifier = Modifier.height(10.dp))
        PokemonWeightAndHeightSection(weight = pokemon.weight!!, height = pokemon.height!!)
        Spacer(modifier = Modifier.height(10.dp))
        PokemonStatsSection(pokemon = pokemon)
    }
}

@Composable
fun PokemonWeightAndHeightSection(
    weight: Int,
    height: Int,
    sectionHeight: Dp = 80.dp
) {
    val weightInKg = remember {
        (weight * 100f) / 1000f
    }
    val heightInM = remember {
        (height * 100f) / 1000f
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        PokemonDetailsData(
            dataValue = weightInKg,
            dataUnit = "kg",
            dataIcon = painterResource(id = R.drawable.ic_weight),
            modifier = Modifier.weight(1f)
        )
        Spacer(
            modifier = Modifier
                .size(1.dp, sectionHeight)
                .background(Color.LightGray)
        )
        PokemonDetailsData(
            dataValue = heightInM,
            dataUnit = "m",
            dataIcon = painterResource(id = R.drawable.ic_height),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun PokemonStatsSection(
    pokemon: Pokemon,
    animDelay: Int = 100
) {
    val maxStat = remember {
        pokemon.stats!!.maxOf {
            it.baseStat!!
        }
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Base Stat",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        pokemon.stats!!.forEachIndexed { index, stat ->
            PokemonDetailsStat(
                baseStat = stat.baseStat!!,
                maxStat = maxStat,
                statName = PokemonParse().parseStatToAbbr(stat),
                statsColor = PokemonParse().parseStatToColor(stat),
                delayDur = index * animDelay
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun PokemonDetailsStat(
    baseStat: Int,
    maxStat: Int,
    statName: String,
    height: Dp = 28.dp,
    statsColor: Color,
    animDur: Int = 1000,
    delayDur: Int = 0
) {
    var isPlayed by remember {
        mutableStateOf(false)
    }
    val width = animateFloatAsState(
        targetValue = if (isPlayed) {
            baseStat / maxStat.toFloat()
        } else 0f,
        animationSpec = tween(
            durationMillis = animDur,
            delayMillis = delayDur
        )
    )

    LaunchedEffect(key1 = true) {
        isPlayed = true
    }

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .fillMaxWidth()
            .height(height)
            .background(
                if (isSystemInDarkTheme()) {
                    Color(0xFF505050)
                } else Color.LightGray
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(width.value)
                .clip(CircleShape)
                .background(
                    statsColor
                )
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = statName,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = (width.value * maxStat).toInt().toString(),
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun PokemonDetailsData(
    dataValue: Float,
    dataUnit: String,
    dataIcon: Painter,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(painter = dataIcon, contentDescription = null, tint = MaterialTheme.colors.onSurface)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$dataValue$dataUnit",
            color = MaterialTheme.colors.onSurface,
            fontSize = 18.sp
        )
    }
}

@Composable
fun PokemonTypeSection(
    types: List<Type>,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        types.forEach { type ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp, vertical = 8.dp)
                    .clip(CircleShape)
                    .background(PokemonParse().parseTypeToColor(type))
                    .height(35.dp)
            ) {
                Text(
                    text = type.type!!.name!!.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    },
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }
}


@Composable
fun DetailsPokemon(
    pokemon: Resource<Pokemon>,
    pokemonName: String,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier,
    detailsViewModel: PokemonDetailsViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    when (pokemon) {
        is Resource.Success -> {
            PokemonDetailsSection(
                pokemon = pokemon.data!!,
                modifier = modifier.offset(y = -(20).dp)
            )
        }
        is Resource.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = loadingModifier
            )
        }
        else -> {
            Retry(error = pokemon.message!!) {
                scope.launch {
                    detailsViewModel.getPokemonDetail(pokemonName)
                }
            }
        }
    }
}

@Composable
fun PokemonImage(
    pokemon: Resource<Pokemon>,
    pokemonName: String,
) {
    rememberCoroutineScope()
    if (pokemon is Resource.Success) {
        SubcomposeAsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(pokemon.data!!.sprites!!.frontDefault)
                .crossfade(true).build(),
            contentDescription = pokemonName,
            modifier = Modifier
                .size(200.dp)
                .offset(y = 20.dp),
            loading = {
                CircularProgressIndicator(
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.scale(0.5f)
                )
            }
        )
    }
}

@Composable
fun TopSection(
    navController: NavController,
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black,
                        Color.Transparent
                    )
                )
            )
            .padding(vertical = 10.dp)
            .offset(
                16.dp,
                16.dp
            )
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .size(36.dp)
                .clickable {
                    navController.popBackStack()
                },
            tint = Color.White
        )
    }
}




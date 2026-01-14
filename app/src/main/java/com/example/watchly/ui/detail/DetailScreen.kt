package com.example.watchly.ui.detail

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.watchly.data.remote.response.ItemDetailResponse
import com.example.watchly.ui.theme.WatchlyTheme

@Composable
fun DetailScreen(
    viewModel: DetailScreenViewModel = hiltViewModel(),
    itemId: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(itemId) {
        viewModel.loadDetails(itemId)
    }

    DetailScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onRetry = { viewModel.loadDetails(itemId) },
        onErrorShown = { viewModel.clearError() },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenContent(
    uiState: DetailScreenUiState,
    onBackClick: () -> Unit = {},
    onRetry: () -> Unit = {},
    onErrorShown: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            val result = snackbarHostState.showSnackbar(
                message = error,
                actionLabel = "Retry",
                duration = SnackbarDuration.Long
            )
            if (result == SnackbarResult.ActionPerformed) {
                onRetry()
            }
            onErrorShown()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.details?.title ?: "Details", overflow = TextOverflow.Ellipsis, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),

            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    DetailShimmer()
                }

                uiState.details != null -> {
                    DetailContent(details = uiState.details)
                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Failed to load details")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = onRetry) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailContent(
    details: ItemDetailResponse,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Poster Image
        AsyncImage(
            model = details.poster ?: details.backdrop,
            contentDescription = details.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(350.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Title
            Text(
                text = details.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Year & Type Row
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                details.year?.let { year ->
                    Text(
                        text = "$year",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                details.type?.let { type ->
                    val typeFormatted=when(type){
                        "movie"->"Movie"
                        "tv_series"->"Tv Show"
                        else->type
                    }
                    Text(
                        text = " •$typeFormatted",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                details.runtimeMinutes?.let { runtime ->
                    Text(
                        text = " • ${runtime} min",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Release Date
            details.releaseDate?.let { date ->
                Text(
                    text = "Release Date: $date",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Rating
            details.userRating?.let { rating ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⭐ $rating/10",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Genres
            details.genreNames?.let { genres ->
                if (genres.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        genres.take(3).forEach { genre ->
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(genre)
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Description
            Text(
                text = "Description",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = details.plotOverview ?: "No description available.",
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.3
            )
        }
    }
}


@Composable
fun DetailShimmer() {
    val brush = detailShimmerBrush()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Poster shimmer
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .padding(8.dp)
                .background(brush)
        )

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Title shimmer
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(28.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Year & Type shimmer
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(18.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Release date shimmer
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Rating shimmer
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .height(18.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Genres shimmer
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(3) {
                    Spacer(
                        modifier = Modifier
                            .width(70.dp)
                            .height(32.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(brush)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description title shimmer
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.35f)
                    .height(22.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description lines shimmer
            repeat(5) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(if (it == 4) 0.6f else 1f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(brush)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun detailShimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "detail_shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "detail_shimmer"
    )
    return Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.9f),
            Color.LightGray.copy(alpha = 0.3f),
            Color.LightGray.copy(alpha = 0.9f)
        ),
        start = Offset(translateAnim.value - 500f, translateAnim.value - 500f),
        end = Offset(translateAnim.value, translateAnim.value)
    )
}


@Preview(showSystemUi = true)
@Composable
private fun DetailScreenContentPreview() {
    WatchlyTheme {
        DetailScreenContent(
            uiState = DetailScreenUiState(
                isLoading = false,
                details = ItemDetailResponse(
                    id = 1,
                    backdrop = null,
                    title = "Inception",
                    type = "movie",
                    poster = null,
                    plotOverview = "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
                    genreNames = listOf("Action", "Sci-Fi", "Thriller"),
                    runtimeMinutes = 148,
                    year = 2010,
                    releaseDate = "2010-07-16",
                    userRating = 8.8,
                    criticScore = 74,
                    trailer = null
                )
            )
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DetailScreenLoadingPreview() {
    WatchlyTheme {
        DetailScreenContent(
            uiState = DetailScreenUiState(isLoading = true)
        )
    }
}



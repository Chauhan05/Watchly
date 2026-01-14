package com.example.watchly.ui.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.watchly.data.remote.response.Title
import com.example.watchly.ui.theme.WatchlyTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreenContent(
        uiState = uiState,
        onItemClick = onItemClick,
        onRetry = { viewModel.loadData() },
        onErrorShown = { viewModel.clearError() },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    uiState: HomeScreenUiState,
    onItemClick: (Int) -> Unit,
    onRetry: () -> Unit = {},
    onErrorShown: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Show error in Snackbar
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
                title = { Text("Watchly") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tabs
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                ) {
                    Text("Movies", modifier = Modifier.padding(16.dp))
                }
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                ) {
                    Text("TV Shows", modifier = Modifier.padding(16.dp))
                }
            }

            // Content
            when {
                uiState.isLoading -> {
                    ShimmerList()
                }

                else -> {
                    val items = if (selectedTab == 0) uiState.movies else uiState.tvShows

                    if (items.isEmpty()) {
//                        Box(
//                            modifier = Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text("No items found")
//
//                        }

                        Column(
                            modifier=Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("No items found")

                            Spacer(modifier=Modifier.padding(4.dp))
                            IconButton(
                                onClick = onRetry
                            ) {
                                Icon(Icons.Default.Refresh,"referesh",modifier=Modifier.size(50.dp))
                            }
                        }
                    } else {
                        LazyColumn {
                            items(items) { title ->
                                TitleItem(
                                    title = title,
                                    onClick = { onItemClick(title.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TitleItem(
    title: Title,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Year: ${title.year ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            title.type?.let { type ->

                val typeFormatted=when(type){
                    "movie"->"Movie"
                    "tv_series"->"Tv Show"
                    else->type
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = typeFormatted,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomeScreenContentPreview() {
    WatchlyTheme {
        HomeScreenContent(
            uiState = HomeScreenUiState(
                isLoading = false,
                movies = listOf(
                    Title(
                        id = 1, title = "Inception", year = 2010, type = "movie",
                        imdbId = "",
                        tmdbId = 0,
                        tmdbType = ""
                    ),
                    Title(
                        id = 2, title = "The Dark Knight", year = 2008, type = "movie",
                        imdbId = "",
                        tmdbId = 1,
                        tmdbType = ""
                    )
                ),
                tvShows = emptyList()
            ),
            onItemClick = {}
        )
    }
}

@Composable
fun ShimmerList(count: Int = 10) {
    LazyColumn {
        items(count) {
            ShimmerListItem()
        }
    }
}

@Composable
fun ShimmerListItem() {
    val brush = shimmerBrush()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Title shimmer
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Year shimmer
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.35f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Type shimmer
            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .height(14.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )
        }
    }
}

@Composable
fun shimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
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
private fun HomeScreenContentLoadingPreview() {
    WatchlyTheme {
        HomeScreenContent(
            uiState = HomeScreenUiState(isLoading = true),
            onItemClick = {}
        )
    }
}
package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.viewmodel.AnimeTrackerViewModel

@Composable
fun MainContainer(
    viewModel: AnimeTrackerViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val watchlist by viewModel.watchlist.collectAsStateWithLifecycle()
    val recommendations by viewModel.recommendations.collectAsStateWithLifecycle()
    val isLoadingMoreRecommendations by viewModel.isLoadingMoreRecommendations.collectAsStateWithLifecycle()

    val selectedAnimeId by viewModel.selectedAnimeId.collectAsStateWithLifecycle()
    val selectedAnimeDetails by viewModel.selectedAnimeDetails.collectAsStateWithLifecycle()
    val selectedUserAnime by viewModel.selectedUserAnime.collectAsStateWithLifecycle()

    // Handle custom system back button to close DetailScreen overlay
    if (selectedAnimeId != null) {
        BackHandler {
            viewModel.selectAnime(null)
        }
    }

    Scaffold(
        bottomBar = {
            if (selectedAnimeId == null) {
                NavigationBar(
                    containerColor = Color(0xFF030304),
                    contentColor = Color.White,
                    tonalElevation = 8.dp,
                    modifier = Modifier.testTag("main_navigation_bar")
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == 0) Icons.Filled.Search else Icons.Outlined.Search,
                                contentDescription = "Поиск"
                            )
                        },
                        label = { Text("Поиск") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color.White,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier.testTag("tab_explore")
                    )

                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = {
                            Icon(
                                imageVector = if (selectedTab == 1) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                contentDescription = "Мой список"
                            )
                        },
                        label = { Text("Мой список") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color.White,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        modifier = Modifier.testTag("tab_watchlist")
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets.navigationBars
    ) { innerPadding ->
        // Premium AMOLED Slate background with ambient soft Indigo/Rose glowing flare
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF050505))
                .drawBehind {
                    // Draw a subtle soft Indigo halo
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0x2A6366F1), // Indigo halo
                                Color(0x00000000)
                            ),
                            center = androidx.compose.ui.geometry.Offset(size.width * 0.85f, size.height * 0.12f),
                            radius = size.width * 0.95f
                        )
                    )
                    // Draw a subtle soft Rose accent halo
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0x18F43F5E), // Muted rose
                                Color(0x00000000)
                            ),
                            center = androidx.compose.ui.geometry.Offset(size.width * 0.15f, size.height * 0.88f),
                            radius = size.width * 1.1f
                        )
                    )
                }
                .padding(innerPadding)
        ) {
            if (selectedAnimeId != null) {
                DetailScreen(
                    anime = selectedAnimeDetails,
                    userAnime = selectedUserAnime,
                    recommendations = recommendations,
                    onBack = { viewModel.selectAnime(null) },
                    onUpdateWatchlist = { anime, status, score, progress, note ->
                        viewModel.updateWatchlist(anime, status, score, progress, note)
                    },
                    onRemoveFromWatchlist = { id ->
                        viewModel.removeFromWatchlist(id)
                        viewModel.selectAnime(null)
                    },
                    onAnimeSelect = { id ->
                        viewModel.selectAnime(id)
                    }
                )
            } else {
                when (selectedTab) {
                    0 -> ExploreScreen(
                        searchQuery = searchQuery,
                        isSearching = isSearching,
                        searchResults = searchResults,
                        recommendations = recommendations,
                        isLoadingMore = isLoadingMoreRecommendations,
                        onLoadMore = { viewModel.loadMoreRecommendations() },
                        onSearchChanged = { viewModel.onSearchQueryChanged(it) },
                        onAnimeSelect = { viewModel.selectAnime(it) }
                    )
                    1 -> WatchlistScreen(
                        watchlistList = watchlist,
                        onAnimeSelect = { viewModel.selectAnime(it) },
                        onProgressIncrement = { userAnime ->
                            viewModel.updateWatchlist(
                                anime = userAnime.anime,
                                status = userAnime.status,
                                userScore = userAnime.userScore,
                                episodesWatched = userAnime.episodesWatched + 1,
                                notes = userAnime.notes
                            )
                        },
                        onDelete = { viewModel.removeFromWatchlist(it) }
                    )
                }
            }
        }
    }
}

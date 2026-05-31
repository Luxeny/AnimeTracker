package com.example.app.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.app.viewmodel.AnimeTrackerViewModel
import com.example.core.navigation.AppDestination
import com.example.core.navigation.FeatureNavigator
import com.example.feature.auth.ui.LoginScreen
import com.example.feature.auth.service.TokenStorage
import com.example.feature.about.ui.AboutScreen
import org.koin.compose.koinInject
import com.example.feature.detail.ui.DetailScreen
import com.example.feature.explore.ui.ExploreScreen
import com.example.feature.watchlist.ui.WatchlistScreen

@Composable
fun MainContainer(
    viewModel: AnimeTrackerViewModel,
    navigator: FeatureNavigator,
    modifier: Modifier = Modifier
) {
    val tokenStorage: TokenStorage = koinInject()
    var selectedTab by remember { mutableStateOf(0) }
    var isLoggedIn by remember { mutableStateOf(tokenStorage.getUser() != null) }

    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val watchlist by viewModel.watchlist.collectAsStateWithLifecycle()
    val recommendations by viewModel.recommendations.collectAsStateWithLifecycle()
    val isLoadingMoreRecommendations by viewModel.isLoadingMoreRecommendations.collectAsStateWithLifecycle()

    val selectedAnimeId by viewModel.selectedAnimeId.collectAsStateWithLifecycle()
    val selectedAnimeDetails by viewModel.selectedAnimeDetails.collectAsStateWithLifecycle()
    val selectedUserAnime by viewModel.selectedUserAnime.collectAsStateWithLifecycle()

    if (!isLoggedIn) {
        LoginScreen(onLoginSuccess = { isLoggedIn = true })
    } else {
        if (selectedAnimeId != null) {
            BackHandler {
                navigator.navigateBackFromDetail()
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
                            onClick = {
                                selectedTab = 0
                                navigator.navigateTo(AppDestination.Explore)
                            },
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
                            onClick = {
                                selectedTab = 1
                                navigator.navigateTo(AppDestination.Watchlist)
                            },
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

                        NavigationBarItem(
                            selected = selectedTab == 2,
                            onClick = {
                                selectedTab = 2
                            },
                            icon = {
                                Icon(
                                    imageVector = if (selectedTab == 2) Icons.Filled.Info else Icons.Outlined.Info,
                                    contentDescription = "О нас"
                                )
                            },
                            label = { Text("О нас") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                indicatorColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = Color.Gray,
                                unselectedTextColor = Color.Gray
                            )
                        )
                    }
                }
            },
            contentWindowInsets = WindowInsets.navigationBars
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF050505))
                    .drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0x2A6366F1), Color(0x00000000)),
                                center = androidx.compose.ui.geometry.Offset(size.width * 0.85f, size.height * 0.12f),
                                radius = size.width * 0.95f
                            )
                        )
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0x18F43F5E), Color(0x00000000)),
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
                        onBack = { navigator.navigateBackFromDetail() },
                        onUpdateWatchlist = { anime, status, score, progress, note ->
                            viewModel.updateWatchlist(anime, status, score, progress, note)
                        },
                        onRemoveFromWatchlist = { id ->
                            viewModel.removeFromWatchlist(id)
                            navigator.navigateBackFromDetail()
                        },
                        onAnimeSelect = { id -> navigator.navigateTo(AppDestination.Detail(id)) }
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
                            onAnimeSelect = { id -> navigator.navigateTo(AppDestination.Detail(id)) }
                        )

                        1 -> WatchlistScreen(
                            watchlistList = watchlist,
                            onAnimeSelect = { id -> navigator.navigateTo(AppDestination.Detail(id)) },
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

                        2 -> AboutScreen()
                    }
                }
            }
        }
    }
}

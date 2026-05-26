package com.example.feature.explore.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.core.model.Anime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    searchQuery: String,
    isSearching: Boolean,
    searchResults: List<Anime>,
    recommendations: List<Anime>,
    isLoadingMore: Boolean,
    onLoadMore: () -> Unit,
    onSearchChanged: (String) -> Unit,
    onAnimeSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val gridState = rememberLazyGridState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = gridState.layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false
            lastVisibleItem.index >= gridState.layoutInfo.totalItemsCount - 2
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && !isLoadingMore && searchQuery.isEmpty()) {
            onLoadMore()
        }
    }

    LazyVerticalGrid(
        state = gridState,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // 1. App Header (Full Span)
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "DASHBOARD",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "AnimeTracker",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = (-0.5).sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Твой личный список аниме и рекомендации",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // 2. Custom Search Bar with Thin Glass Borders (Full Span)
        item(span = { GridItemSpan(maxLineSpan) }) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChanged,
                placeholder = { Text("Поиск аниме на Shikimori...", color = Color(0xFF94A3B8), fontSize = 14.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChanged("") }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = Color.Gray
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color(0x1BFFFFFF),
                    focusedContainerColor = Color(0x0CFFFFFF),
                    unfocusedContainerColor = Color(0x0CFFFFFF),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = Color.Gray
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("anime_search_input")
            )
        }

        // 3. Featured Hero Card in Bold Typography Design Layout (Full Span)
        if (searchQuery.isEmpty() && recommendations.isNotEmpty()) {
            val featuredAnime = recommendations.first()
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .border(1.dp, Color(0x1EFFFFFF), RoundedCornerShape(32.dp))
                        .background(Color(0xFF14151B))
                        .clickable { onAnimeSelect(featuredAnime.id) }
                ) {
                    // Hero Poster Image
                    AsyncImage(
                        model = featuredAnime.imageUrl,
                        contentDescription = featuredAnime.russian,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Overlay with double gradients for maximum cinematic readability
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0x70000000),
                                        Color(0x22000000),
                                        Color(0xD0000000)
                                    )
                                )
                            )
                    )

                    // Hero Badge overlay at top-left
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "РЕКОМЕНДУЕМ",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = 1.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .background(Color(0xB2000000), RoundedCornerShape(20.dp))
                                .border(1.dp, Color(0x20FFFFFF), RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "EP ${featuredAnime.episodes}",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Lower Content text blocks (Frieren-style italics typography)
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp)
                    ) {
                        Text(
                            text = featuredAnime.russian.uppercase(),
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black,
                            lineHeight = 26.sp,
                            letterSpacing = (-0.5).sp,
                            fontStyle = FontStyle.Italic,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = String.format("%.2f", featuredAnime.score),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "•  " + (featuredAnime.genres.firstOrNull() ?: "Аниме"),
                                color = Color(0xFFCBD5E1),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // 4. Section Heading Title (Full Span)
        item(span = { GridItemSpan(maxLineSpan) }) {
            val titleText = if (searchQuery.isNotEmpty()) "РЕЗУЛЬТАТЫ ПОИСКА" else "РЕКОМЕНДАЦИИ ДЛЯ ТЕБЯ"
            Text(
                text = titleText,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF94A3B8),
                letterSpacing = 2.sp,
                modifier = Modifier.padding(top = 12.dp, start = 4.dp, bottom = 4.dp)
            )
        }

        // 5. Scroll Grid Rows
        if (searchQuery.isNotEmpty()) {
            if (isSearching) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            } else if (searchResults.isEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Ничего не найдено", color = Color.Gray, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("Попробуйте изменить запрос", color = Color.DarkGray, fontSize = 12.sp)
                        }
                    }
                }
            } else {
                items(searchResults, key = { it.id }) { anime ->
                    AnimeCard(anime = anime, onClick = { onAnimeSelect(anime.id) })
                }
            }
        } else {
            // Drop first item as it is already beautifully featured as Hero card above
            val remainingRecs = if (recommendations.isNotEmpty()) recommendations.drop(1) else recommendations
            items(remainingRecs, key = { it.id }) { anime ->
                AnimeCard(anime = anime, onClick = { onAnimeSelect(anime.id) })
            }
            if (isLoadingMore) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@Composable
fun AnimeCard(
    anime: Anime,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0x0CFFFFFF))
            .border(1.dp, Color(0x16FFFFFF), RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
            .testTag("anime_card_${anime.id}")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        ) {
            // Poster Image
            AsyncImage(
                model = anime.imageUrl,
                contentDescription = anime.russian,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            )

            // Rating Badge overlay
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(Color(0xB2000000), RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0x20FFFFFF), RoundedCornerShape(12.dp))
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = String.format("%.1f", anime.score),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Kind/Episode overlay bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xCC000000))
                        )
                    )
                    .padding(8.dp)
            ) {
                Text(
                    text = "${anime.kind.uppercase()} • ${anime.episodes} ЭП.",
                    color = Color(0xFFE2E2E2),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Title & Genres Info
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = anime.russian,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = anime.genres.firstOrNull() ?: "Аниме",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                letterSpacing = 0.5.sp
            )
        }
    }
}

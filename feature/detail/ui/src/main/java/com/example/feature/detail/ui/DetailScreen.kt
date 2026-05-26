package com.example.feature.detail.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.core.model.Anime
import com.example.core.model.UserAnime
import com.example.core.model.WatchStatus

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    anime: Anime?,
    userAnime: UserAnime?,
    recommendations: List<Anime>,
    onBack: () -> Unit,
    onUpdateWatchlist: (Anime, WatchStatus, Int?, Int, String) -> Unit,
    onRemoveFromWatchlist: (Int) -> Unit,
    onAnimeSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    if (anime == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    var showEditDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF050505))
    ) {
        // Blur background poster for immersive premium layered feel
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
        ) {
            AsyncImage(
                model = anime.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .blur(20.dp)
                    .alpha(0.25f)
            )

            // Dynamic bottom fade values to merge blur back to slate black
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color(0xFF050505))
                        )
                    )
            )
        }

        // Main Content Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Action Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .background(Color(0x22FFFFFF), CircleShape)
                        .border(1.dp, Color(0x16FFFFFF), CircleShape)
                        .testTag("detail_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Text(
                    text = "ДЕТАЛИ ПРОИЗВЕДЕНИЯ",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp
                )

                // Empty container for balanced structure
                Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {}
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Hero visual - Banner Poster & Titles Side-by-Side
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                // Large primary poster image with sleek container corners
                AsyncImage(
                    model = anime.imageUrl,
                    contentDescription = anime.russian,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(125.dp)
                        .height(185.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(24.dp))
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Basic details + status fields
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = anime.kind.uppercase(),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = anime.russian,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        fontStyle = FontStyle.Normal,
                        lineHeight = 26.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = anime.name,
                        color = Color(0xFF64748B),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Shikimori score block
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Score",
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = String.format("%.2f", anime.score),
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "/ 10",
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${anime.episodes} ЭП. • СТАТУС: ${if (anime.status == "released") "ВЫШЕЛ" else "ОНГОИНГ"}",
                        color = Color(0xFF94A3B8),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Genres chip row Wrap
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                anime.genres.forEach { genre ->
                    Box(
                        modifier = Modifier
                            .background(Color(0x0CFFFFFF), RoundedCornerShape(16.dp))
                            .border(1.dp, Color(0x12FFFFFF), RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = genre.uppercase(),
                            color = Color(0xFFCBD5E1),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // User tracking integration overview
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                if (userAnime != null) {
                    // Added to watchlist card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color(0x0CFFFFFF))
                            .border(1.dp, Color(0x16FFFFFF), RoundedCornerShape(24.dp))
                            .padding(16.dp)
                            .testTag("user_anime_summary_card")
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Bookmark,
                                        contentDescription = "Status",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "В твоем списке:",
                                        color = Color(0xFF64748B),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Text(
                                    text = userAnime.status.russianName.uppercase(),
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.5.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Пройдено серий:",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "${userAnime.episodesWatched} / ${if (anime.episodes > 0) anime.episodes else "?"}",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            if (userAnime.userScore != null) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Твоя оценка:",
                                        color = Color(0xFF94A3B8),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "Selected Stars",
                                            tint = Color(0xFFFFC107),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "${userAnime.userScore} / 10",
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            if (userAnime.notes.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                HorizontalDivider(color = Color(0x10FFFFFF))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "ТВОЯ ЗАМЕТКА",
                                    color = Color(0xFF64748B),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = userAnime.notes,
                                    color = Color(0xFFCBD5E1),
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { onRemoveFromWatchlist(anime.id) },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFF43F5E)),
                                    border = BorderStroke(1.dp, Color(0xFFF43F5E).copy(alpha = 0.5f)),
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .testTag("delete_watchlist_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Удалить", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = { showEditDialog = true },
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .testTag("edit_watchlist_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Изменить", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else {
                    // Not in watchlist block
                    Button(
                        onClick = { showEditDialog = true },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("add_to_watchlist_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "Bookmark list",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Добавить в список просмотра",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Synopsis description
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "ОПИСАНИЕ СЮЖЕТА",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = anime.description,
                    color = Color(0xFFE2E2E2),
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Justify
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Related custom recommendations row
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 80.dp)
            ) {
                Text(
                    text = "ПОХОЖИЕ ПРОИЗВЕДЕНИЯ",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("details_recommendations_row"),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    val filteredRecs = recommendations.filter { it.id != anime.id }
                    items(filteredRecs) { recAnime ->
                        Column(
                            modifier = Modifier
                                .width(115.dp)
                                .clickable { onAnimeSelect(recAnime.id) }
                        ) {
                            AsyncImage(
                                model = recAnime.imageUrl,
                                contentDescription = recAnime.russian,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(155.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .border(1.dp, Color(0x16FFFFFF), RoundedCornerShape(20.dp))
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = recAnime.russian,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }

    // Anime Status modal editor dialog
    if (showEditDialog) {
        AnimeTrackingEditDialog(
            anime = anime,
            initialUserAnime = userAnime,
            onDismiss = { showEditDialog = false },
            onSave = { status, score, progress, note ->
                onUpdateWatchlist(anime, status, score, progress, note)
                showEditDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeTrackingEditDialog(
    anime: Anime,
    initialUserAnime: UserAnime?,
    onDismiss: () -> Unit,
    onSave: (WatchStatus, Int?, Int, String) -> Unit
) {
    var selectedStatus by remember { mutableStateOf(initialUserAnime?.status ?: WatchStatus.PLAN_TO_WATCH) }
    var scoreValue by remember { mutableStateOf(initialUserAnime?.userScore?.toFloat() ?: 8f) }
    var setScoreEnabled by remember { mutableStateOf(initialUserAnime?.userScore != null) }
    var episodesWatched by remember { mutableStateOf(initialUserAnime?.episodesWatched ?: 0) }
    var episodesInput by remember { mutableStateOf((initialUserAnime?.episodesWatched ?: 0).toString()) }
    var personalNotes by remember { mutableStateOf(initialUserAnime?.notes ?: "") }

    LaunchedEffect(selectedStatus) {
        if (selectedStatus == WatchStatus.COMPLETED && anime.episodes > 0) {
            episodesWatched = anime.episodes
            episodesInput = anime.episodes.toString()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(Color(0xFF0F0F12))
                .border(1.dp, Color(0x1CFFFFFF), RoundedCornerShape(28.dp))
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "ИЗМЕНЕНИЕ СТАТУСА",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = anime.russian,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Статус:",
                    color = Color(0xFF64748B),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Scrollable status chips or dual grids
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    WatchStatus.values().toList().chunked(2).forEach { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            pair.forEach { status ->
                                val isSelected = selectedStatus == status
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(if (isSelected) MaterialTheme.colorScheme.primary else Color(0x11FFFFFF))
                                        .border(1.dp, if (isSelected) Color.Transparent else Color(0x10FFFFFF), RoundedCornerShape(16.dp))
                                        .clickable { selectedStatus = status }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = status.russianName.uppercase(),
                                        color = if (isSelected) Color.White else Color.LightGray,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 0.5.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Progress episode Counter
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Просмотрено серий:",
                            color = Color(0xFF94A3B8),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Всего серий: ${if (anime.episodes > 0) anime.episodes else "неизвестно"}",
                            color = Color.Gray,
                            fontSize = 11.sp
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (episodesWatched > 0) {
                                    episodesWatched--
                                    episodesInput = episodesWatched.toString()
                                }
                            },
                            modifier = Modifier
                                .background(Color(0x15FFFFFF), CircleShape)
                                .size(36.dp)
                        ) {
                            Text("-", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedTextField(
                            value = episodesInput,
                            onValueChange = { raw ->
                                episodesInput = raw.filter { it.isDigit() }
                                episodesWatched = episodesInput.toIntOrNull()?.let { value ->
                                    if (anime.episodes > 0) value.coerceIn(0, anime.episodes) else value.coerceAtLeast(0)
                                } ?: 0
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = LocalTextStyle.current.copy(
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = Color(0x1BFFFFFF),
                                focusedContainerColor = Color(0x0CFFFFFF),
                                unfocusedContainerColor = Color(0x0CFFFFFF)
                            ),
                            modifier = Modifier
                                .width(72.dp)
                                .padding(horizontal = 6.dp)
                        )

                        IconButton(
                            onClick = {
                                if (anime.episodes == 0 || episodesWatched < anime.episodes) {
                                    episodesWatched++
                                    episodesInput = episodesWatched.toString()
                                }
                            },
                            modifier = Modifier
                                .background(Color(0x15FFFFFF), CircleShape)
                                .size(36.dp)
                        ) {
                            Text("+", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Custom rating toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = setScoreEnabled,
                        onCheckedChange = { setScoreEnabled = it },
                        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                    )
                    Text(
                        text = "Оценить аниме",
                        color = Color(0xFFE2E2E2),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (setScoreEnabled) {
                    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Оценка:", color = Color.Gray, fontSize = 12.sp)
                            Text("${scoreValue.toInt()}/10", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Slider(
                            value = scoreValue,
                            onValueChange = { scoreValue = it },
                            valueRange = 1f..10f,
                            steps = 8,
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colorScheme.primary,
                                activeTrackColor = MaterialTheme.colorScheme.primary,
                                inactiveTrackColor = Color(0x15FFFFFF)
                            )
                        )
                    }
                }

                // Personal Review Notes Option
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Заметка / Комментарий:",
                    color = Color(0xFF64748B),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                OutlinedTextField(
                    value = personalNotes,
                    onValueChange = { personalNotes = it },
                    placeholder = { Text("Забавные моменты, мысли...", color = Color.DarkGray, fontSize = 12.sp) },
                    singleLine = false,
                    maxLines = 3,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color(0x1BFFFFFF),
                        focusedContainerColor = Color(0x0CFFFFFF),
                        unfocusedContainerColor = Color(0x0CFFFFFF)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(86.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Footer Actions inside dialog
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.LightGray),
                        border = BorderStroke(1.dp, Color(0x1BFFFFFF)),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                    ) {
                        Text("Отмена", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            onSave(
                                selectedStatus,
                                if (setScoreEnabled) scoreValue.toInt() else null,
                                episodesWatched,
                                personalNotes
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("save_tracking_btn")
                    ) {
                        Text("Сохранить", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

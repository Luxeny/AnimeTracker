package com.example.domain.repository

import com.example.domain.model.Anime
import com.example.domain.model.UserAnime
import kotlinx.coroutines.flow.Flow

interface AnimeRepository {
    fun searchAnimes(query: String): Flow<List<Anime>>
    fun getAnimeDetails(id: Int): Flow<Anime?>
    fun getRecommendations(): Flow<List<Anime>>
    fun getRandomAnimes(page: Int): Flow<List<Anime>>
    fun getWatchlist(): Flow<List<UserAnime>>
    fun getWatchlistAnime(id: Int): Flow<UserAnime?>
    suspend fun saveToWatchlist(userAnime: UserAnime)
    suspend fun deleteFromWatchlist(id: Int)
}

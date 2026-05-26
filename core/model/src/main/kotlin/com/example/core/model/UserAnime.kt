package com.example.core.model

data class UserAnime(
    val anime: Anime,
    val status: WatchStatus,
    val userScore: Int? = null,
    val episodesWatched: Int = 0,
    val notes: String = "",
    val lastUpdated: Long = System.currentTimeMillis()
)

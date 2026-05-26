package com.example.core.model

data class Anime(
    val id: Int,
    val name: String,
    val russian: String,
    val imageUrl: String,
    val score: Double,
    val episodes: Int,
    val episodesAired: Int,
    val status: String,
    val kind: String,
    val genres: List<String>,
    val description: String
)

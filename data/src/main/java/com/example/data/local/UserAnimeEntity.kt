package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.Anime
import com.example.domain.model.UserAnime
import com.example.domain.model.WatchStatus

@Entity(tableName = "user_watchlist")
data class UserAnimeEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val russian: String,
    val imageUrl: String,
    val score: Double,
    val episodes: Int,
    val episodesAired: Int,
    val status: String,
    val kind: String,
    val genresCsv: String,
    val description: String,
    val watchStatus: String,
    val userScore: Int?,
    val episodesWatched: Int,
    val notes: String,
    val lastUpdated: Long
) {
    fun toDomain(): UserAnime {
        return UserAnime(
            anime = Anime(
                id = id,
                name = name,
                russian = russian,
                imageUrl = imageUrl,
                score = score,
                episodes = episodes,
                episodesAired = episodesAired,
                status = status,
                kind = kind,
                genres = if (genresCsv.isEmpty()) emptyList() else genresCsv.split(","),
                description = description
            ),
            status = WatchStatus.valueOf(watchStatus),
            userScore = userScore,
            episodesWatched = episodesWatched,
            notes = notes,
            lastUpdated = lastUpdated
        )
    }

    companion object {
        fun fromDomain(domain: UserAnime): UserAnimeEntity {
            return UserAnimeEntity(
                id = domain.anime.id,
                name = domain.anime.name,
                russian = domain.anime.russian,
                imageUrl = domain.anime.imageUrl,
                score = domain.anime.score,
                episodes = domain.anime.episodes,
                episodesAired = domain.anime.episodesAired,
                status = domain.anime.status,
                kind = domain.anime.kind,
                genresCsv = domain.anime.genres.joinToString(","),
                description = domain.anime.description,
                watchStatus = domain.status.name,
                userScore = domain.userScore,
                episodesWatched = domain.episodesWatched,
                notes = domain.notes,
                lastUpdated = domain.lastUpdated
            )
        }
    }
}

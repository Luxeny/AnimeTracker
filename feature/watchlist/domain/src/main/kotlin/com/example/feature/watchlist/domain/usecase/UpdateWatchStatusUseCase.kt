package com.example.feature.watchlist.domain.usecase

import com.example.core.model.Anime
import com.example.core.model.UserAnime
import com.example.core.model.WatchStatus
import com.example.core.model.repository.AnimeRepository
import com.example.core.model.util.EpisodeProgressCalculator

class UpdateWatchStatusUseCase(private val repository: AnimeRepository) {
  suspend operator fun invoke(userAnime: UserAnime) {
    val normalized = userAnime.copy(
      episodesWatched = EpisodeProgressCalculator.resolveEpisodesWatched(
        anime = userAnime.anime,
        status = userAnime.status,
        requestedEpisodes = userAnime.episodesWatched
      ),
      lastUpdated = System.currentTimeMillis()
    )
    repository.saveToWatchlist(normalized)
  }

  suspend operator fun invoke(
    anime: Anime,
    status: WatchStatus,
    userScore: Int?,
    episodesWatched: Int,
    notes: String
  ) {
    invoke(
      UserAnime(
        anime = anime,
        status = status,
        userScore = userScore,
        episodesWatched = episodesWatched,
        notes = notes
      )
    )
  }
}

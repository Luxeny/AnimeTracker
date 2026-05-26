package com.example.core.model.util

import com.example.core.model.Anime
import com.example.core.model.WatchStatus

object EpisodeProgressCalculator {
  fun resolveEpisodesWatched(anime: Anime, status: WatchStatus, requestedEpisodes: Int): Int {
    val normalized = requestedEpisodes.coerceAtLeast(0)
    if (status == WatchStatus.COMPLETED && anime.episodes > 0) {
      return anime.episodes
    }
    return if (anime.episodes > 0) normalized.coerceAtMost(anime.episodes) else normalized
  }
}

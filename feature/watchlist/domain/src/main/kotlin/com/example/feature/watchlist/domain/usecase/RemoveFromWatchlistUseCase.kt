package com.example.feature.watchlist.domain.usecase

import com.example.core.model.repository.AnimeRepository

class RemoveFromWatchlistUseCase(private val repository: AnimeRepository) {
  suspend operator fun invoke(id: Int) {
    repository.deleteFromWatchlist(id)
  }
}

package com.example.domain.usecase

import com.example.domain.repository.AnimeRepository

class RemoveFromWatchlistUseCase(private val repository: AnimeRepository) {
  suspend operator fun invoke(id: Int) {
    repository.deleteFromWatchlist(id)
  }
}

package com.example.domain.usecase

import com.example.domain.model.UserAnime
import com.example.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow

class GetWatchlistAnimeUseCase(private val repository: AnimeRepository) {
  operator fun invoke(id: Int): Flow<UserAnime?> = repository.getWatchlistAnime(id)
}

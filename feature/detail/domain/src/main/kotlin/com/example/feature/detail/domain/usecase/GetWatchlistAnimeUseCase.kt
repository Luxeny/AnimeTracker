package com.example.feature.detail.domain.usecase

import com.example.core.model.UserAnime
import com.example.core.model.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow

class GetWatchlistAnimeUseCase(private val repository: AnimeRepository) {
  operator fun invoke(id: Int): Flow<UserAnime?> = repository.getWatchlistAnime(id)
}

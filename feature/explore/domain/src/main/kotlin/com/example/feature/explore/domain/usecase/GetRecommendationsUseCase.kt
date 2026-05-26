package com.example.feature.explore.domain.usecase

import com.example.core.model.Anime
import com.example.core.model.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetRecommendationsUseCase(private val repository: AnimeRepository) {
  operator fun invoke(): Flow<List<Anime>> = repository.getRecommendations()
}

class LoadInitialRecommendationsUseCase(private val repository: AnimeRepository) {
  operator fun invoke(): Flow<List<Anime>> {
    return repository.getRecommendations().map { recommendations -> recommendations.shuffled() }
  }
}

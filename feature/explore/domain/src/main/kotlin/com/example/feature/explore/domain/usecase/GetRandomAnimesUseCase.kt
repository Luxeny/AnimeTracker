package com.example.feature.explore.domain.usecase

import com.example.core.model.Anime
import com.example.core.model.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow

class GetRandomAnimesUseCase(private val repository: AnimeRepository) {
    operator fun invoke(page: Int): Flow<List<Anime>> {
        return repository.getRandomAnimes(page)
    }
}

package com.example.feature.detail.domain.usecase

import com.example.core.model.Anime
import com.example.core.model.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow

class GetAnimeDetailsUseCase(private val repository: AnimeRepository) {
    operator fun invoke(id: Int): Flow<Anime?> {
        return repository.getAnimeDetails(id)
    }
}

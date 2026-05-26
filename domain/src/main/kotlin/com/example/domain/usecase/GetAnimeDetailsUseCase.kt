package com.example.domain.usecase

import com.example.domain.model.Anime
import com.example.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow

class GetAnimeDetailsUseCase(private val repository: AnimeRepository) {
    operator fun invoke(id: Int): Flow<Anime?> {
        return repository.getAnimeDetails(id)
    }
}

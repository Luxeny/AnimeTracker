package com.example.domain.usecase

import com.example.domain.model.Anime
import com.example.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow

class GetRandomAnimesUseCase(private val repository: AnimeRepository) {
    operator fun invoke(page: Int): Flow<List<Anime>> {
        return repository.getRandomAnimes(page)
    }
}

package com.example.domain.usecase

import com.example.domain.model.Anime
import com.example.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow

class SearchAnimeUseCase(private val repository: AnimeRepository) {
    operator fun invoke(query: String): Flow<List<Anime>> {
        return repository.searchAnimes(query)
    }
}

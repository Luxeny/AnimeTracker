package com.example.domain.usecase

import com.example.domain.model.Anime
import com.example.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow

class SearchAnimeUseCase(private val repository: AnimeRepository) {
    operator fun invoke(query: String): Flow<List<Anime>> {
        val trimmedQuery = query.trim()
        if (trimmedQuery.length < 3 && trimmedQuery.isNotEmpty()) {
            return kotlinx.coroutines.flow.flowOf(emptyList())
        }
        return repository.searchAnimes(trimmedQuery)
    }
}

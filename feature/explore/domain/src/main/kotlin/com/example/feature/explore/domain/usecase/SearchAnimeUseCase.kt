package com.example.feature.explore.domain.usecase

import com.example.core.model.Anime
import com.example.core.model.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow

class SearchAnimeUseCase(private val repository: AnimeRepository) {
    operator fun invoke(query: String): Flow<List<Anime>> {
        return repository.searchAnimes(query)
    }
}

package com.example.domain.usecase

import com.example.domain.model.UserAnime
import com.example.domain.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow

class GetWatchlistUseCase(private val repository: AnimeRepository) {
    operator fun invoke(): Flow<List<UserAnime>> {
        return repository.getWatchlist()
    }
}

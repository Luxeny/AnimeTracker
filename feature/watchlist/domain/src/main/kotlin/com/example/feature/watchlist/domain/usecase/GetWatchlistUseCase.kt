package com.example.feature.watchlist.domain.usecase

import com.example.core.model.UserAnime
import com.example.core.model.repository.AnimeRepository
import kotlinx.coroutines.flow.Flow

class GetWatchlistUseCase(private val repository: AnimeRepository) {
    operator fun invoke(): Flow<List<UserAnime>> {
        return repository.getWatchlist()
    }
}

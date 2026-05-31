package com.example.feature.watchlist.domain.di

import com.example.feature.watchlist.domain.usecase.GetWatchlistUseCase
import com.example.feature.watchlist.domain.usecase.RemoveFromWatchlistUseCase
import com.example.feature.watchlist.domain.usecase.UpdateWatchStatusUseCase
import org.koin.dsl.module

val watchlistDomainModule = module {
    factory { GetWatchlistUseCase(get()) }
    factory { UpdateWatchStatusUseCase(get()) }
    factory { RemoveFromWatchlistUseCase(get()) }
}

package com.example.app.di

import com.example.app.viewmodel.AnimeTrackerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        AnimeTrackerViewModel(
            searchAnimeUseCase = get(),
            getAnimeDetailsUseCase = get(),
            getWatchlistUseCase = get(),
            getWatchlistAnimeUseCase = get(),
            updateWatchStatusUseCase = get(),
            removeFromWatchlistUseCase = get(),
            loadInitialRecommendationsUseCase = get(),
            getRandomAnimesUseCase = get(),
            analyticsService = get()
        )
    }
}

package com.example.ui.di

import com.example.ui.viewmodel.AnimeTrackerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
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
    )
  }
}

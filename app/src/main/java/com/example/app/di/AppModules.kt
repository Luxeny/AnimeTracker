package com.example.app.di

import com.example.app.viewmodel.AnimeTrackerViewModel
import com.example.data.di.dataModule
import com.example.domain.repository.AnimeRepository
import com.example.domain.usecase.GetAnimeDetailsUseCase
import com.example.domain.usecase.GetRandomAnimesUseCase
import com.example.domain.usecase.GetWatchlistAnimeUseCase
import com.example.domain.usecase.GetWatchlistUseCase
import com.example.domain.usecase.LoadInitialRecommendationsUseCase
import com.example.domain.usecase.RemoveFromWatchlistUseCase
import com.example.domain.usecase.SearchAnimeUseCase
import com.example.domain.usecase.UpdateWatchStatusUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val domainModule = module {
  factory { SearchAnimeUseCase(get()) }
  factory { GetAnimeDetailsUseCase(get()) }
  factory { GetWatchlistUseCase(get()) }
  factory { GetWatchlistAnimeUseCase(get()) }
  factory { UpdateWatchStatusUseCase(get()) }
  factory { RemoveFromWatchlistUseCase(get()) }
  factory { LoadInitialRecommendationsUseCase(get()) }
  factory { GetRandomAnimesUseCase(get()) }
}

val appModule = module {
  includes(dataModule, domainModule)
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

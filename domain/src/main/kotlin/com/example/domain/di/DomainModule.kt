package com.example.domain.di

import com.example.domain.usecase.*
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

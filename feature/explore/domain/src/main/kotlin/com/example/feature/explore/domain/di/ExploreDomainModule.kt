package com.example.feature.explore.domain.di

import com.example.feature.explore.domain.usecase.GetRandomAnimesUseCase
import com.example.feature.explore.domain.usecase.LoadInitialRecommendationsUseCase
import com.example.feature.explore.domain.usecase.SearchAnimeUseCase
import org.koin.dsl.module

val exploreDomainModule = module {
    factory { SearchAnimeUseCase(get()) }
    factory { LoadInitialRecommendationsUseCase(get()) }
    factory { GetRandomAnimesUseCase(get()) }
}

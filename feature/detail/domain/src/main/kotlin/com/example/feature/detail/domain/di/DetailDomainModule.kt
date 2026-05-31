package com.example.feature.detail.domain.di

import com.example.feature.detail.domain.usecase.GetAnimeDetailsUseCase
import com.example.feature.detail.domain.usecase.GetWatchlistAnimeUseCase
import org.koin.dsl.module

val detailDomainModule = module {
    factory { GetAnimeDetailsUseCase(get()) }
    factory { GetWatchlistAnimeUseCase(get()) }
}

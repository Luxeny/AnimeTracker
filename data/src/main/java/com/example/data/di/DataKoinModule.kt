package com.example.data.di

import com.example.data.local.AppDatabase
import com.example.data.remote.RetrofitClient
import com.example.data.repository.AnimeRepositoryImpl
import com.example.domain.repository.AnimeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
  single { AppDatabase.getDatabase(androidContext()) }
  single { get<AppDatabase>().userAnimeDao }
  single { RetrofitClient.apiService }
  single<AnimeRepository> { AnimeRepositoryImpl(get(), get()) }
}

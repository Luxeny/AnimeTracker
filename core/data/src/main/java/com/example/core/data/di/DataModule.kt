package com.example.core.data.di

import com.example.core.data.local.AppDatabase
import com.example.core.data.remote.RetrofitClient
import com.example.core.data.repository.AnimeRepositoryImpl
import com.example.core.model.repository.AnimeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
  single { AppDatabase.getDatabase(androidContext()) }
  single { get<AppDatabase>().userAnimeDao }
  single { RetrofitClient.apiService }
  single<AnimeRepository> {
    AnimeRepositoryImpl(
      apiService = get(),
      userAnimeDao = get()
    )
  }
}

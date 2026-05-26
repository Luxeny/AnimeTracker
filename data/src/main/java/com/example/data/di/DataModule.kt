package com.example.data.di

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.remote.RetrofitClient
import com.example.data.repository.AnimeRepositoryImpl
import com.example.domain.repository.AnimeRepository

object DataModule {
  fun provideAnimeRepository(context: Context): AnimeRepository {
    val database = AppDatabase.getDatabase(context.applicationContext)
    return AnimeRepositoryImpl(
      apiService = RetrofitClient.apiService,
      userAnimeDao = database.userAnimeDao
    )
  }
}

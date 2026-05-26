package com.example.core.data.di

import android.content.Context
import com.example.core.data.local.AppDatabase
import com.example.core.data.remote.RetrofitClient
import com.example.core.data.repository.AnimeRepositoryImpl
import com.example.core.model.repository.AnimeRepository

object DataModule {
  fun provideAnimeRepository(context: Context): AnimeRepository {
    val database = AppDatabase.getDatabase(context.applicationContext)
    return AnimeRepositoryImpl(
      apiService = RetrofitClient.apiService,
      userAnimeDao = database.userAnimeDao
    )
  }
}

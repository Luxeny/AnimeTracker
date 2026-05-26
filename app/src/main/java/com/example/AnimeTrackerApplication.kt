package com.example

import android.app.Application
import com.example.app.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AnimeTrackerApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    startKoin {
      androidContext(this@AnimeTrackerApplication)
      modules(appModule)
    }
  }
}

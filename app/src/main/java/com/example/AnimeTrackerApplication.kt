package com.example

import android.app.Application
import com.example.data.di.dataModule
import com.example.domain.di.domainModule
import com.example.ui.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AnimeTrackerApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    startKoin {
      androidLogger()
      androidContext(this@AnimeTrackerApplication)
      modules(listOf(dataModule, domainModule, uiModule))
    }
  }
}

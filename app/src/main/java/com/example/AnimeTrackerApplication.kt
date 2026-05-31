package com.example

import android.app.Application
import com.example.app.di.appModule
import com.example.core.data.di.dataModule
import com.example.feature.detail.domain.di.detailDomainModule
import com.example.feature.explore.domain.di.exploreDomainModule
import com.example.feature.watchlist.domain.di.watchlistDomainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AnimeTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@AnimeTrackerApplication)
            modules(
                listOf(
                    appModule,
                    dataModule,
                    exploreDomainModule,
                    detailDomainModule,
                    watchlistDomainModule
                )
            )
        }
    }
}

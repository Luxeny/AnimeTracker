package com.example

import android.app.ActivityManager
import android.app.Application
import android.os.Process
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.example.app.di.appModule
import com.example.core.analytics.di.analyticsModule
import com.example.feature.auth.di.authModule
import com.example.feature.about.di.aboutModule
import com.example.core.constant.AppConstants
import com.example.core.data.di.dataModule
import com.example.feature.detail.domain.di.detailDomainModule
import com.example.feature.explore.domain.di.exploreDomainModule
import com.example.feature.watchlist.domain.di.watchlistDomainModule
import com.yandex.mapkit.MapKitFactory
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AnimeTrackerApplication : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()
        
        if (isMainProcess()) {
            MapKitFactory.setApiKey(BuildConfig.YANDEX_MAPS_API_KEY)
            MapKitFactory.initialize(this)

            startKoin {
                androidLogger()
                androidContext(this@AnimeTrackerApplication)
                modules(
                    listOf(
                        appModule,
                        dataModule,
                        exploreDomainModule,
                        detailDomainModule,
                        watchlistDomainModule,
                        analyticsModule,
                        authModule,
                        aboutModule,
                    )
                )
            }
        }
    }

    private fun isMainProcess(): Boolean {
        val am = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val processes = am.runningAppProcesses
        val myPid = Process.myPid()
        if (processes != null) {
            for (process in processes) {
                if (process.pid == myPid) {
                    return process.processName == packageName
                }
            }
        }
        return false
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .okHttpClient {
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .header("User-Agent", AppConstants.USER_AGENT)
                            .build()
                        chain.proceed(request)
                    }
                    .build()
            }
            .crossfade(enable = true)
            .build()
    }
}

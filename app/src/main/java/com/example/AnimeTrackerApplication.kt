package com.example

import android.app.Application
import coil.ImageLoader
import android.os.Build
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
        
        com.yandex.mapkit.MapKitFactory.setApiKey(BuildConfig.YANDEX_MAPS_API_KEY)
        com.yandex.mapkit.MapKitFactory.initialize(this)

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
                    aboutModule
                )
            )
        }
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
            .crossfade(true)
            .build()
    }
}

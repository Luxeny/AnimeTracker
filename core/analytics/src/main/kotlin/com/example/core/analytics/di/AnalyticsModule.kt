package com.example.core.analytics.di

import com.example.core.analytics.service.AnalyticsService
import com.example.core.analytics.service.AppMetricaAnalyticsService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val analyticsModule = module {
    single<AnalyticsService> { AppMetricaAnalyticsService(androidContext()) }
}

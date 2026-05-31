package com.example.core.analytics.service

import android.content.Context
import com.example.core.analytics.BuildConfig
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig

class AppMetricaAnalyticsService(context: Context) : AnalyticsService {
    
    init {
        val config = AppMetricaConfig.newConfigBuilder(BuildConfig.APPMETRICA_API_KEY).build()
        AppMetrica.activate(context.applicationContext, config)
    }

    override fun trackEvent(name: String, params: Map<String, Any>) {
        AppMetrica.reportEvent(name, params)
    }

    override fun trackError(message: String, error: Throwable?) {
        AppMetrica.reportError(message, error)
    }
}

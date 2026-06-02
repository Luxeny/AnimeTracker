package com.example.core.analytics.service

import io.appmetrica.analytics.AppMetrica

class AppMetricaAnalyticsService : AnalyticsService {

    override fun trackEvent(name: String, params: Map<String, Any>) {
        AppMetrica.reportEvent(name, params)
    }

    override fun trackError(message: String, error: Throwable?) {
        AppMetrica.reportError(message, error)
    }
}

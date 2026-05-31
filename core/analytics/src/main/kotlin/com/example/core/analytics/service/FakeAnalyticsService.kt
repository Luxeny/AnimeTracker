package com.example.core.analytics.service

import android.util.Log

class FakeAnalyticsService : AnalyticsService {
    override fun trackEvent(name: String, params: Map<String, Any>) {
        Log.d("FakeAnalytics", "Event tracked: $name with params: $params")
    }

    override fun trackError(message: String, error: Throwable?) {
        Log.d("FakeAnalytics", "Error tracked: $message", error)
    }
}

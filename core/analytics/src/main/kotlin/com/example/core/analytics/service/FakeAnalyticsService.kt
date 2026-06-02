package com.example.core.analytics.service

import android.util.Log

class FakeAnalyticsService : AnalyticsService {
    val trackedEvents = mutableListOf<Pair<String, Map<String, Any>>>()
    val trackedErrors = mutableListOf<Pair<String, Throwable?>>()

    override fun trackEvent(name: String, params: Map<String, Any>) {
        trackedEvents.add(name to params)
        Log.d("FakeAnalytics", "Event: $name, Params: $params")
    }

    override fun trackError(message: String, error: Throwable?) {
        trackedErrors.add(message to error)
        Log.e("FakeAnalytics", "Error: $message", error)
    }
}

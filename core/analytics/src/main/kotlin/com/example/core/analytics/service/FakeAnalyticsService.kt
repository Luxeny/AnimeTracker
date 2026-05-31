package com.example.core.analytics.service

class FakeAnalyticsService : AnalyticsService {
    val trackedEvents = mutableListOf<Pair<String, Map<String, Any>>>()

    override fun trackEvent(name: String, params: Map<String, Any>) {
        trackedEvents.add(name to params)
        println("FakeAnalytics: Event tracked: $name with params: $params")
    }

    override fun trackError(message: String, error: Throwable?) {
        println("FakeAnalytics: Error tracked: $message")
    }
}

package com.example.domain.model

enum class WatchStatus(val displayName: String, val russianName: String) {
    PLAN_TO_WATCH("Plan to Watch", "В планах"),
    WATCHING("Watching", "Смотрю"),
    COMPLETED("Completed", "Просмотрено"),
    DROPPED("Dropped", "Брошено")
}

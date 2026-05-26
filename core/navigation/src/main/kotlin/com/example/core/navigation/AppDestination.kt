package com.example.core.navigation

sealed interface AppDestination {
  data object Explore : AppDestination
  data object Watchlist : AppDestination
  data class Detail(val animeId: Int) : AppDestination
}

interface FeatureNavigator {
  fun navigateTo(destination: AppDestination)
  fun navigateBackFromDetail()
}

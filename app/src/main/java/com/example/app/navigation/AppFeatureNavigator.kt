package com.example.app.navigation

import com.example.app.viewmodel.AnimeTrackerViewModel
import com.example.core.navigation.AppDestination
import com.example.core.navigation.FeatureNavigator

class AppFeatureNavigator(
  private val viewModel: AnimeTrackerViewModel
) : FeatureNavigator {
  override fun navigateTo(destination: AppDestination) {
    when (destination) {
      AppDestination.Explore,
      AppDestination.Watchlist -> viewModel.selectAnime(null)

      is AppDestination.Detail -> viewModel.selectAnime(destination.animeId)
    }
  }

  override fun navigateBackFromDetail() {
    viewModel.selectAnime(null)
  }
}

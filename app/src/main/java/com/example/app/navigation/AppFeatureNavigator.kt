package com.example.app.navigation

import com.example.app.viewmodel.AnimeTrackerViewModel
import com.example.core.navigation.AppDestination
import com.example.core.navigation.FeatureNavigator

class AppFeatureNavigator(
  private val viewModel: AnimeTrackerViewModel
) : FeatureNavigator {
  override fun navigateTo(destination: AppDestination) {
    when (destination) {
      AppDestination.Explore -> {
        viewModel.selectAnime(null)
        viewModel.trackScreen("Explore")
      }
      AppDestination.Watchlist -> {
        viewModel.selectAnime(null)
        viewModel.trackScreen("Watchlist")
      }
      is AppDestination.Detail -> {
        viewModel.selectAnime(destination.animeId)
        viewModel.trackScreen("Detail")
      }
    }
  }

  override fun navigateBackFromDetail() {
    viewModel.selectAnime(null)
  }
}

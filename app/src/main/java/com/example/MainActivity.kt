package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.app.navigation.AppFeatureNavigator
import com.example.app.navigation.MainContainer
import com.example.app.viewmodel.AnimeTrackerViewModel
import com.example.core.data.di.DataModule
import com.example.core.model.repository.AnimeRepository
import com.example.feature.detail.domain.usecase.GetAnimeDetailsUseCase
import com.example.feature.detail.domain.usecase.GetWatchlistAnimeUseCase
import com.example.feature.explore.domain.usecase.GetRandomAnimesUseCase
import com.example.feature.explore.domain.usecase.LoadInitialRecommendationsUseCase
import com.example.feature.explore.domain.usecase.SearchAnimeUseCase
import com.example.feature.watchlist.domain.usecase.GetWatchlistUseCase
import com.example.feature.watchlist.domain.usecase.RemoveFromWatchlistUseCase
import com.example.feature.watchlist.domain.usecase.UpdateWatchStatusUseCase
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val repository: AnimeRepository = DataModule.provideAnimeRepository(applicationContext)
    val viewModel: AnimeTrackerViewModel by viewModels {
      AnimeTrackerViewModel.Factory(
        searchAnimeUseCase = SearchAnimeUseCase(repository),
        getAnimeDetailsUseCase = GetAnimeDetailsUseCase(repository),
        getWatchlistUseCase = GetWatchlistUseCase(repository),
        getWatchlistAnimeUseCase = GetWatchlistAnimeUseCase(repository),
        updateWatchStatusUseCase = UpdateWatchStatusUseCase(repository),
        removeFromWatchlistUseCase = RemoveFromWatchlistUseCase(repository),
        loadInitialRecommendationsUseCase = LoadInitialRecommendationsUseCase(repository),
        getRandomAnimesUseCase = GetRandomAnimesUseCase(repository),
      )
    }

    setContent {
      val navigator = AppFeatureNavigator(viewModel)
      MyApplicationTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          MainContainer(viewModel = viewModel, navigator = navigator)
        }
      }
    }
  }
}

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
import com.example.data.di.DataModule
import com.example.domain.repository.AnimeRepository
import com.example.domain.usecase.GetAnimeDetailsUseCase
import com.example.domain.usecase.GetRandomAnimesUseCase
import com.example.domain.usecase.GetWatchlistAnimeUseCase
import com.example.domain.usecase.GetWatchlistUseCase
import com.example.domain.usecase.LoadInitialRecommendationsUseCase
import com.example.domain.usecase.RemoveFromWatchlistUseCase
import com.example.domain.usecase.SearchAnimeUseCase
import com.example.domain.usecase.UpdateWatchStatusUseCase
import com.example.ui.screens.MainContainer
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AnimeTrackerViewModel

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
      MyApplicationTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          MainContainer(viewModel = viewModel)
        }
      }
    }
  }
}

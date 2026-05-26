package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.app.navigation.AppFeatureNavigator
import com.example.app.navigation.MainContainer
import com.example.app.viewmodel.AnimeTrackerViewModel
import com.example.ui.theme.MyApplicationTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      val viewModel: AnimeTrackerViewModel = koinViewModel()
      val navigator = AppFeatureNavigator(viewModel)

      MyApplicationTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          MainContainer(viewModel = viewModel, navigator = navigator)
        }
      }
    }
  }
}

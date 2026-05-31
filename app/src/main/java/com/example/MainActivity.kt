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
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
  private val viewModel: AnimeTrackerViewModel by viewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

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

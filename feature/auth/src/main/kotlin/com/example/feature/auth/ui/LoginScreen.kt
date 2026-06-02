package com.example.feature.auth.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.feature.auth.viewmodel.AuthState
import com.example.feature.auth.viewmodel.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel()
) {
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            onLoginSuccess()
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "Вход в Anime Tracker", style = MaterialTheme.typography.headlineMedium)

            Button(
                onClick = { viewModel.loginWithYandex() },
                modifier = Modifier.fillMaxWidth(0.8f),
                enabled = authState !is AuthState.Loading
            ) {
                Text("Войти через Яндекс")
            }

            Button(
                onClick = { viewModel.loginWithVk() },
                modifier = Modifier.fillMaxWidth(0.8f),
                enabled = authState !is AuthState.Loading
            ) {
                Text("Войти через VK")
            }

            if (authState is AuthState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }

            if (authState is AuthState.Error) {
                Text(
                    text = (authState as AuthState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

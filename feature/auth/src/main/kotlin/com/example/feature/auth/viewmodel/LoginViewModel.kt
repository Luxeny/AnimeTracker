package com.example.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.analytics.service.AnalyticsService
import com.example.feature.auth.service.AuthService
import com.example.feature.auth.service.AuthUser
import com.example.feature.auth.service.TokenStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authService: AuthService,
    private val tokenStorage: TokenStorage,
    private val analyticsService: AnalyticsService
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    fun login() {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            authService.login()
                .onSuccess { user ->
                    tokenStorage.saveUser(user)
                    analyticsService.trackEvent("user_logged_in", mapOf("provider" to user.provider))
                    _authState.value = AuthState.Success(user)
                }
                .onFailure { error ->
                    analyticsService.trackError("Login failed", error)
                    _authState.value = AuthState.Error(error.message ?: "Unknown error")
                }
        }
    }
}

sealed interface AuthState {
    data object Idle : AuthState
    data object Loading : AuthState
    data class Success(val user: AuthUser) : AuthState
    data class Error(val message: String) : AuthState
}

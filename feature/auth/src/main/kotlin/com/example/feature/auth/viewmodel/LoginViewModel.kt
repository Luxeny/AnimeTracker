package com.example.feature.auth.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.analytics.service.AnalyticsService
import com.example.feature.auth.service.AuthResult
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

    fun loginWithYandex(activity: Activity) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            handleResult(authService.loginWithYandex(activity))
        }
    }

    fun loginWithVk(activity: Activity) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            handleResult(authService.loginWithVk(activity))
        }
    }

    private fun handleResult(result: AuthResult) {
        when (result) {
            is AuthResult.Success -> {
                tokenStorage.saveUser(result.user)
                analyticsService.trackEvent("user_logged_in", mapOf("provider" to result.user.provider))
                _authState.value = AuthState.Success(result.user)
            }
            is AuthResult.Error -> {
                analyticsService.trackError("Login failed", Exception(result.message))
                _authState.value = AuthState.Error(result.message)
            }
            AuthResult.Cancelled -> {
                _authState.value = AuthState.Idle
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

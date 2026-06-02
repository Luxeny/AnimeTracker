package com.example.feature.auth.service

import android.app.Activity

interface AuthService {
    suspend fun loginWithYandex(activity: Activity): AuthResult
    suspend fun loginWithVk(activity: Activity): AuthResult
    fun logout()
    fun getCurrentUser(): AuthUser?
}

sealed interface AuthResult {
    data class Success(val user: AuthUser) : AuthResult
    data class Error(val message: String) : AuthResult
    data object Cancelled : AuthResult
}

data class AuthUser(
    val id: String,
    val name: String,
    val token: String,
    val provider: String
)

package com.example.feature.auth.service

interface AuthService {
    suspend fun loginWithYandex(): AuthResult
    suspend fun loginWithVk(): AuthResult
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

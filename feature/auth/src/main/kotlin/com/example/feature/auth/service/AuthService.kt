package com.example.feature.auth.service

interface AuthService {
    suspend fun login(): Result<AuthUser>
    fun logout()
    fun getCurrentUser(): AuthUser?
}

data class AuthUser(
    val id: String,
    val name: String,
    val token: String,
    val provider: String
)

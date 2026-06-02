package com.example.feature.auth.service

import android.app.Activity
import com.example.feature.auth.service.AuthResult
import com.example.feature.auth.service.AuthService
import com.example.feature.auth.service.AuthUser

class AuthServiceImpl : AuthService {
    override suspend fun loginWithYandex(): AuthResult {
        // Real SDK implementation would go here
        return AuthResult.Success(AuthUser("yandex_123", "Yandex User", "token_abc", "yandex"))
    }

    override suspend fun loginWithVk(): AuthResult {
        // Real SDK implementation would go here
        return AuthResult.Success(AuthUser("vk_456", "VK User", "token_xyz", "vk"))
    }

    override fun logout() {
        // Real SDK logout
    }

    override fun getCurrentUser(): AuthUser? = null
}

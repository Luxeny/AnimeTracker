package com.example.feature.auth.service

class VkAuthService : AuthService {
    override suspend fun login(): Result<AuthUser> {
        // Simplified mock for lab purposes
        return Result.success(AuthUser("vk_456", "VK User", "vk_token_xyz", "vk"))
    }

    override fun logout() {
        // VK Logout logic
    }

    override fun getCurrentUser(): AuthUser? = null
}

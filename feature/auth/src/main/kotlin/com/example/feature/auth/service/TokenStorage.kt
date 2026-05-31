package com.example.feature.auth.service

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class TokenStorage(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveUser(user: AuthUser) {
        sharedPreferences.edit()
            .putString("user_id", user.id)
            .putString("user_name", user.name)
            .putString("user_token", user.token)
            .putString("user_provider", user.provider)
            .apply()
    }

    fun getUser(): AuthUser? {
        val id = sharedPreferences.getString("user_id", null) ?: return null
        val name = sharedPreferences.getString("user_name", "") ?: ""
        val token = sharedPreferences.getString("user_token", "") ?: ""
        val provider = sharedPreferences.getString("user_provider", "") ?: ""
        return AuthUser(id, name, token, provider)
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}

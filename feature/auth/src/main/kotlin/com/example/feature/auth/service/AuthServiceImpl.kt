package com.example.feature.auth.service

import android.app.Activity
import androidx.activity.ComponentActivity
import com.example.feature.auth.service.AuthResult
import com.example.feature.auth.service.AuthService
import com.example.feature.auth.service.AuthUser
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthServiceImpl : AuthService {
    
    override suspend fun loginWithYandex(activity: Activity): AuthResult {
        return suspendCoroutine { continuation ->
            try {
                val sdk = YandexAuthSdk.create(YandexAuthOptions(activity.applicationContext))
                val loginOptions = YandexAuthLoginOptions()
                val intent = sdk.contract.createIntent(activity, loginOptions)
                
                activity.startActivity(intent) 
                
                continuation.resume(AuthResult.Success(AuthUser("yandex_123", "Yandex User", "token_abc", "yandex")))
            } catch (e: Exception) {
                continuation.resume(AuthResult.Error(e.message ?: "Yandex Login failed"))
            }
        }
    }

    override suspend fun loginWithVk(activity: Activity): AuthResult {
        return suspendCoroutine { continuation ->
            try {
                val componentActivity = activity as? ComponentActivity
                if (componentActivity != null) {
                    val launcher = VK.login(componentActivity) { _ -> }
                    launcher.launch(arrayListOf(VKScope.EMAIL))
                }
                
                continuation.resume(AuthResult.Success(AuthUser("vk_456", "VK User", "vk_token_xyz", "vk")))
            } catch (e: Exception) {
                continuation.resume(AuthResult.Error(e.message ?: "VK Login failed"))
            }
        }
    }

    override fun logout() {
        VK.logout()
    }

    override fun getCurrentUser(): AuthUser? = null
}

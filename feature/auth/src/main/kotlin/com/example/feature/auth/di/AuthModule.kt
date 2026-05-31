package com.example.feature.auth.di

import com.example.feature.auth.service.AuthService
import com.example.feature.auth.service.TokenStorage
import com.example.feature.auth.viewmodel.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

// Initial implementation of AuthService for laboratory purposes
class MockAuthService : AuthService {
    override suspend fun login(): Result<com.example.feature.auth.service.AuthUser> {
        return Result.success(
            com.example.feature.auth.service.AuthUser("1", "User", "test_token", "yandex")
        )
    }
    override fun logout() {}
    override fun getCurrentUser(): com.example.feature.auth.service.AuthUser? = null
}

val authModule = module {
    single { TokenStorage(androidContext()) }
    single<AuthService> { MockAuthService() }
    viewModel { LoginViewModel(get(), get(), get()) }
}

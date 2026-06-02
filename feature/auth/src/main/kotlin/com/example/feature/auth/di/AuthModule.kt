package com.example.feature.auth.di

import com.example.feature.auth.service.AuthService
import com.example.feature.auth.service.AuthServiceImpl
import com.example.feature.auth.service.TokenStorage
import com.example.feature.auth.viewmodel.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single { TokenStorage(androidContext()) }
    single<AuthService> { AuthServiceImpl() }
    viewModel { LoginViewModel(get(), get(), get()) }
}

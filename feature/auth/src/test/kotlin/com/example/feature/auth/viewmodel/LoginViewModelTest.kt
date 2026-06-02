package com.example.feature.auth.viewmodel

import com.example.core.analytics.service.FakeAnalyticsService
import com.example.feature.auth.service.AuthResult
import com.example.feature.auth.service.AuthService
import com.example.feature.auth.service.AuthUser
import com.example.feature.auth.service.TokenStorage
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val authService = mockk<AuthService>()
    private val tokenStorage = mockk<TokenStorage>(relaxed = true)
    private val analyticsService = FakeAnalyticsService()
    private val testDispatcher = StandardTestDispatcher()
    
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(authService, tokenStorage, analyticsService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when yandex login successful then track user_logged_in event`() {
        val user = AuthUser("1", "Test User", "token", "yandex")
        coEvery { authService.loginWithYandex() } returns AuthResult.Success(user)

        viewModel.loginWithYandex()
        testDispatcher.scheduler.advanceUntilIdle()

        val eventSent = analyticsService.trackedEvents.any { 
            it.first == "user_logged_in" && it.second["provider"] == "yandex"
        }
        assertTrue("Analytics event user_logged_in should be sent", eventSent)
    }

    @Test
    fun `when vk login successful then track user_logged_in event`() {
        val user = AuthUser("2", "VK User", "vk_token", "vk")
        coEvery { authService.loginWithVk() } returns AuthResult.Success(user)

        viewModel.loginWithVk()
        testDispatcher.scheduler.advanceUntilIdle()

        val eventSent = analyticsService.trackedEvents.any { 
            it.first == "user_logged_in" && it.second["provider"] == "vk"
        }
        assertTrue("Analytics event user_logged_in should be sent for VK", eventSent)
    }

    @Test
    fun `when login fails then track error`() {
        val errorMessage = "Network Error"
        coEvery { authService.loginWithYandex() } returns AuthResult.Error(errorMessage)

        viewModel.loginWithYandex()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue("Analytics error should be reported", analyticsService.trackedErrors.isNotEmpty())
        assertEquals("Login failed", analyticsService.trackedErrors.first().first)
    }
}

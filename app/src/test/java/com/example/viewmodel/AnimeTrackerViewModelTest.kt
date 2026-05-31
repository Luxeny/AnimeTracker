package com.example.viewmodel

import com.example.app.viewmodel.AnimeTrackerViewModel
import com.example.core.analytics.service.AnalyticsService
import com.example.feature.detail.domain.usecase.GetAnimeDetailsUseCase
import com.example.feature.detail.domain.usecase.GetWatchlistAnimeUseCase
import com.example.feature.explore.domain.usecase.GetRandomAnimesUseCase
import com.example.feature.explore.domain.usecase.LoadInitialRecommendationsUseCase
import com.example.feature.explore.domain.usecase.SearchAnimeUseCase
import com.example.feature.watchlist.domain.usecase.GetWatchlistUseCase
import com.example.feature.watchlist.domain.usecase.RemoveFromWatchlistUseCase
import com.example.feature.watchlist.domain.usecase.UpdateWatchStatusUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Test

class AnimeTrackerViewModelTest {

    private val analyticsService = mockk<AnalyticsService>(relaxed = true)
    private lateinit var viewModel: AnimeTrackerViewModel

    @Before
    fun setup() {
        val loadInitialRecommendationsUseCase = mockk<LoadInitialRecommendationsUseCase>()
        val getWatchlistUseCase = mockk<GetWatchlistUseCase>()
        val searchAnimeUseCase = mockk<SearchAnimeUseCase>()
        
        every { loadInitialRecommendationsUseCase() } returns flowOf(emptyList())
        every { getWatchlistUseCase() } returns flowOf(emptyList())
        every { searchAnimeUseCase(any()) } returns flowOf(emptyList())

        viewModel = AnimeTrackerViewModel(
            searchAnimeUseCase = searchAnimeUseCase,
            getAnimeDetailsUseCase = mockk(),
            getWatchlistUseCase = getWatchlistUseCase,
            getWatchlistAnimeUseCase = mockk(),
            updateWatchStatusUseCase = mockk(),
            removeFromWatchlistUseCase = mockk(),
            loadInitialRecommendationsUseCase = loadInitialRecommendationsUseCase,
            getRandomAnimesUseCase = mockk(),
            analyticsService = analyticsService
        )
    }

    @Test
    fun `when trackScreen is called then analytics event is sent`() {
        viewModel.trackScreen("TestScreen")
        
        verify { 
            analyticsService.trackEvent(
                "screen_viewed", 
                mapOf("screen_name" to "TestScreen")
            ) 
        }
    }

    @Test
    fun `initialization tracks Explore screen`() {
        // Verification happens automatically because setup() calls init {} block
        verify { 
            analyticsService.trackEvent(
                "screen_viewed", 
                mapOf("screen_name" to "Explore")
            ) 
        }
    }
}

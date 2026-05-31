package com.example.viewmodel

import com.example.app.viewmodel.AnimeTrackerViewModel
import com.example.core.analytics.service.FakeAnalyticsService
import com.example.feature.explore.domain.usecase.LoadInitialRecommendationsUseCase
import com.example.feature.explore.domain.usecase.GetRandomAnimesUseCase
import com.example.feature.explore.domain.usecase.SearchAnimeUseCase
import com.example.feature.watchlist.domain.usecase.GetWatchlistUseCase
import com.example.feature.watchlist.domain.usecase.RemoveFromWatchlistUseCase
import com.example.feature.watchlist.domain.usecase.UpdateWatchStatusUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AnimeTrackerViewModelTest {

    private val analyticsService = FakeAnalyticsService()
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
    fun `when trackScreen is called then analytics event is sent via FakeService`() {
        viewModel.trackScreen("TestScreen")
        
        val eventSent = analyticsService.trackedEvents.any { 
            it.first == "screen_viewed" && it.second["screen_name"] == "TestScreen"
        }
        assertTrue("Event should be sent to FakeAnalyticsService", eventSent)
    }

    @Test
    fun `initialization tracks Explore screen via FakeService`() {
        val eventSent = analyticsService.trackedEvents.any { 
            it.first == "screen_viewed" && it.second["screen_name"] == "Explore"
        }
        assertTrue("Initial Explore event should be sent", eventSent)
    }
}

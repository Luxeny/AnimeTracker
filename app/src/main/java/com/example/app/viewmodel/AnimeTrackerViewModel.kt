package com.example.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.core.constant.AppConstants
import com.example.core.model.Anime
import com.example.core.model.UserAnime
import com.example.core.model.WatchStatus
import com.example.feature.detail.domain.usecase.GetAnimeDetailsUseCase
import com.example.feature.detail.domain.usecase.GetWatchlistAnimeUseCase
import com.example.feature.explore.domain.usecase.GetRandomAnimesUseCase
import com.example.feature.explore.domain.usecase.LoadInitialRecommendationsUseCase
import com.example.feature.explore.domain.usecase.SearchAnimeUseCase
import com.example.feature.watchlist.domain.usecase.GetWatchlistUseCase
import com.example.feature.watchlist.domain.usecase.RemoveFromWatchlistUseCase
import com.example.feature.watchlist.domain.usecase.UpdateWatchStatusUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class AnimeTrackerViewModel(
  private val searchAnimeUseCase: SearchAnimeUseCase,
  private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
  private val getWatchlistUseCase: GetWatchlistUseCase,
  private val getWatchlistAnimeUseCase: GetWatchlistAnimeUseCase,
  private val updateWatchStatusUseCase: UpdateWatchStatusUseCase,
  private val removeFromWatchlistUseCase: RemoveFromWatchlistUseCase,
  private val loadInitialRecommendationsUseCase: LoadInitialRecommendationsUseCase,
  private val getRandomAnimesUseCase: GetRandomAnimesUseCase,
) : ViewModel() {

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _isSearching = MutableStateFlow(false)
  val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

  val searchResults: StateFlow<List<Anime>> = _searchQuery
    .debounce(AppConstants.SEARCH_DEBOUNCE_MS)
    .flatMapLatest { query ->
      _isSearching.value = true
      searchAnimeUseCase(query)
    }
    .onEach { _isSearching.value = false }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val watchlist: StateFlow<List<UserAnime>> = getWatchlistUseCase()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  private val _recommendations = MutableStateFlow<List<Anime>>(emptyList())
  val recommendations: StateFlow<List<Anime>> = _recommendations.asStateFlow()

  private val _isLoadingMoreRecommendations = MutableStateFlow(false)
  val isLoadingMoreRecommendations: StateFlow<Boolean> = _isLoadingMoreRecommendations.asStateFlow()

  init {
    viewModelScope.launch {
      loadInitialRecommendationsUseCase().collect { _recommendations.value = it }
    }
  }

  fun loadMoreRecommendations() {
    if (_isLoadingMoreRecommendations.value) return
    _isLoadingMoreRecommendations.value = true
    viewModelScope.launch {
      getRandomAnimesUseCase((2..50).random()).collect { newList ->
        if (newList.isNotEmpty()) {
          val existingIds = _recommendations.value.map { it.id }.toSet()
          _recommendations.value = _recommendations.value + newList.filter { it.id !in existingIds }
        }
        _isLoadingMoreRecommendations.value = false
      }
    }
  }

  private val _selectedAnimeId = MutableStateFlow<Int?>(null)
  val selectedAnimeId: StateFlow<Int?> = _selectedAnimeId.asStateFlow()

  val selectedAnimeDetails: StateFlow<Anime?> = _selectedAnimeId
    .flatMapLatest { id -> if (id != null) getAnimeDetailsUseCase(id) else flowOf(null) }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  val selectedUserAnime: StateFlow<UserAnime?> = _selectedAnimeId
    .flatMapLatest { id -> if (id != null) getWatchlistAnimeUseCase(id) else flowOf(null) }
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  fun onSearchQueryChanged(query: String) { _searchQuery.value = query }
  fun selectAnime(id: Int?) { _selectedAnimeId.value = id }

  fun updateWatchlist(anime: Anime, status: WatchStatus, userScore: Int? = null, episodesWatched: Int = 0, notes: String = "") {
    viewModelScope.launch {
      updateWatchStatusUseCase(anime, status, userScore, episodesWatched, notes)
    }
  }

  fun removeFromWatchlist(id: Int) {
    viewModelScope.launch { removeFromWatchlistUseCase(id) }
  }

  class Factory(
    private val searchAnimeUseCase: SearchAnimeUseCase,
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
    private val getWatchlistUseCase: GetWatchlistUseCase,
    private val getWatchlistAnimeUseCase: GetWatchlistAnimeUseCase,
    private val updateWatchStatusUseCase: UpdateWatchStatusUseCase,
    private val removeFromWatchlistUseCase: RemoveFromWatchlistUseCase,
    private val loadInitialRecommendationsUseCase: LoadInitialRecommendationsUseCase,
    private val getRandomAnimesUseCase: GetRandomAnimesUseCase,
  ) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      if (modelClass.isAssignableFrom(AnimeTrackerViewModel::class.java)) {
        return AnimeTrackerViewModel(
          searchAnimeUseCase, getAnimeDetailsUseCase, getWatchlistUseCase, getWatchlistAnimeUseCase,
          updateWatchStatusUseCase, removeFromWatchlistUseCase, loadInitialRecommendationsUseCase, getRandomAnimesUseCase
        ) as T
      }
      throw IllegalArgumentException("Unknown ViewModel class")
    }
  }
}

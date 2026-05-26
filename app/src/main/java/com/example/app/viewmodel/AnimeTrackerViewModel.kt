package com.example.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.core.constant.AppConstants
import com.example.domain.model.Anime
import com.example.domain.model.UserAnime
import com.example.domain.model.WatchStatus
import com.example.domain.usecase.GetAnimeDetailsUseCase
import com.example.domain.usecase.GetRandomAnimesUseCase
import com.example.domain.usecase.GetWatchlistAnimeUseCase
import com.example.domain.usecase.GetWatchlistUseCase
import com.example.domain.usecase.LoadInitialRecommendationsUseCase
import com.example.domain.usecase.RemoveFromWatchlistUseCase
import com.example.domain.usecase.SearchAnimeUseCase
import com.example.domain.usecase.UpdateWatchStatusUseCase
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
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  val watchlist: StateFlow<List<UserAnime>> = getWatchlistUseCase()
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = emptyList()
    )

  private val _recommendations = MutableStateFlow<List<Anime>>(emptyList())
  val recommendations: StateFlow<List<Anime>> = _recommendations.asStateFlow()

  private val _isLoadingMoreRecommendations = MutableStateFlow(false)
  val isLoadingMoreRecommendations: StateFlow<Boolean> = _isLoadingMoreRecommendations.asStateFlow()

  init {
    viewModelScope.launch {
      loadInitialRecommendationsUseCase().collect { list ->
        _recommendations.value = list
      }
    }
  }

  private var currentRandomPage = 1

  fun loadMoreRecommendations() {
    if (_isLoadingMoreRecommendations.value) return
    _isLoadingMoreRecommendations.value = true
    viewModelScope.launch {
      currentRandomPage++
      val randomPage = (2..50).random()
      getRandomAnimesUseCase(randomPage).collect { newList ->
        if (newList.isNotEmpty()) {
          val existingIds = _recommendations.value.map { it.id }.toSet()
          val filteredNewList = newList.filter { it.id !in existingIds }
          _recommendations.value = _recommendations.value + filteredNewList
        }
        _isLoadingMoreRecommendations.value = false
      }
    }
  }

  private val _selectedAnimeId = MutableStateFlow<Int?>(null)
  val selectedAnimeId: StateFlow<Int?> = _selectedAnimeId.asStateFlow()

  val selectedAnimeDetails: StateFlow<Anime?> = _selectedAnimeId
    .flatMapLatest { id ->
      if (id != null) {
        getAnimeDetailsUseCase(id)
      } else {
        flowOf(null)
      }
    }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = null
    )

  val selectedUserAnime: StateFlow<UserAnime?> = _selectedAnimeId
    .flatMapLatest { id ->
      if (id != null) {
        getWatchlistAnimeUseCase(id)
      } else {
        flowOf(null)
      }
    }
    .stateIn(
      scope = viewModelScope,
      started = SharingStarted.WhileSubscribed(5000),
      initialValue = null
    )

  fun onSearchQueryChanged(query: String) {
    _searchQuery.value = query
  }

  fun selectAnime(id: Int?) {
    _selectedAnimeId.value = id
  }

  fun updateWatchlist(
    anime: Anime,
    status: WatchStatus,
    userScore: Int? = null,
    episodesWatched: Int = 0,
    notes: String = ""
  ) {
    viewModelScope.launch {
      updateWatchStatusUseCase(
        anime = anime,
        status = status,
        userScore = userScore,
        episodesWatched = episodesWatched,
        notes = notes
      )
    }
  }

  fun removeFromWatchlist(id: Int) {
    viewModelScope.launch {
      removeFromWatchlistUseCase(id)
    }
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
          searchAnimeUseCase = searchAnimeUseCase,
          getAnimeDetailsUseCase = getAnimeDetailsUseCase,
          getWatchlistUseCase = getWatchlistUseCase,
          getWatchlistAnimeUseCase = getWatchlistAnimeUseCase,
          updateWatchStatusUseCase = updateWatchStatusUseCase,
          removeFromWatchlistUseCase = removeFromWatchlistUseCase,
          loadInitialRecommendationsUseCase = loadInitialRecommendationsUseCase,
          getRandomAnimesUseCase = getRandomAnimesUseCase,
        ) as T
      }
      throw IllegalArgumentException("Unknown ViewModel class")
    }
  }
}

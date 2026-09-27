package com.movieapp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieapp.data.repo.MovieRepository
import com.movieapp.domain.model.Movie
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<Movie> = emptyList(),
    val error: String? = null,
    val hasSearched: Boolean = false
)

class SearchViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
        searchJob?.cancel()
        if (query.isBlank()) {
            _uiState.value = SearchUiState()
            return
        }
        searchJob = viewModelScope.launch {
            delay(400) // debounce
            search(query)
        }
    }

    private suspend fun search(query: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        repository.searchMovies(query)
            .onSuccess { movies ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    results = movies,
                    hasSearched = true
                )
            }
            .onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Search failed",
                    hasSearched = true
                )
            }
    }
}

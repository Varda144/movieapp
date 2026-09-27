package com.movieapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieapp.data.repo.MovieRepository
import com.movieapp.domain.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val movies: List<Movie> = emptyList(),
    val error: String? = null
)

class HomeViewModel(private val repository: MovieRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadTrending()
    }

    fun loadTrending() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.getTrendingMovies()
                .onSuccess { movies ->
                    _uiState.value = HomeUiState(isLoading = false, movies = movies)
                }
                .onFailure { e ->
                    _uiState.value = HomeUiState(
                        isLoading = false,
                        error = e.message ?: "Failed to load movies"
                    )
                }
        }
    }
}

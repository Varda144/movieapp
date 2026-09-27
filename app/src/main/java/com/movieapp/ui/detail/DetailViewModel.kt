package com.movieapp.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.movieapp.data.repo.MovieRepository
import com.movieapp.domain.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetailUiState(
    val isLoading: Boolean = true,
    val movie: Movie? = null,
    val isFavorite: Boolean = false,
    val error: String? = null
)

class DetailViewModel(
    private val repository: MovieRepository,
    private val movieId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
        observeFavorite()
    }

    private fun loadDetail() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            repository.getMovieDetail(movieId)
                .onSuccess { movie ->
                    _uiState.value = _uiState.value.copy(isLoading = false, movie = movie)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load details"
                    )
                }
        }
    }

    private fun observeFavorite() {
        viewModelScope.launch {
            repository.isFavorite(movieId).collect { isFav ->
                _uiState.value = _uiState.value.copy(isFavorite = isFav)
            }
        }
    }

    fun toggleFavorite() {
        val movie = _uiState.value.movie ?: return
        viewModelScope.launch {
            if (_uiState.value.isFavorite) {
                repository.removeFavorite(movie.id)
            } else {
                repository.addFavorite(movie)
            }
        }
    }
}

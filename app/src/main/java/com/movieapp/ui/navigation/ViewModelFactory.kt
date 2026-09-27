package com.movieapp.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.movieapp.data.repo.MovieRepository
import com.movieapp.ui.detail.DetailViewModel
import com.movieapp.ui.favorites.FavoritesViewModel
import com.movieapp.ui.home.HomeViewModel
import com.movieapp.ui.search.SearchViewModel

class HomeViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}

class SearchViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(repository) as T
    }
}

class DetailViewModelFactory(
    private val repository: MovieRepository,
    private val movieId: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(repository, movieId) as T
    }
}

class FavoritesViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoritesViewModel(repository) as T
    }
}

package com.movieapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.movieapp.MovieApplication
import com.movieapp.data.repo.MovieRepository
import com.movieapp.ui.detail.DetailScreen
import com.movieapp.ui.detail.DetailViewModel
import com.movieapp.ui.favorites.FavoritesScreen
import com.movieapp.ui.favorites.FavoritesViewModel
import com.movieapp.ui.home.HomeScreen
import com.movieapp.ui.home.HomeViewModel
import com.movieapp.ui.search.SearchScreen
import com.movieapp.ui.search.SearchViewModel

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Search : Screen("search")
    data object Detail : Screen("detail/{movieId}") {
        fun createRoute(movieId: Int) = "detail/$movieId"
    }
    data object Favorites : Screen("favorites")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val app = context.applicationContext as MovieApplication
    val repository = remember { app.repository }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            val vm: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(repository)
            )
            HomeScreen(
                viewModel = vm,
                onMovieClick = { movieId ->
                    navController.navigate(Screen.Detail.createRoute(movieId))
                }
            )
        }

        composable(Screen.Search.route) {
            val vm: SearchViewModel = viewModel(
                factory = SearchViewModelFactory(repository)
            )
            SearchScreen(
                viewModel = vm,
                onMovieClick = { movieId ->
                    navController.navigate(Screen.Detail.createRoute(movieId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: return@composable
            val vm: DetailViewModel = viewModel(
                factory = DetailViewModelFactory(repository, movieId)
            )
            DetailScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Favorites.route) {
            val vm: FavoritesViewModel = viewModel(
                factory = FavoritesViewModelFactory(repository)
            )
            FavoritesScreen(
                viewModel = vm,
                onMovieClick = { movieId ->
                    navController.navigate(Screen.Detail.createRoute(movieId))
                }
            )
        }
    }
}

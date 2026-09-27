package com.movieapp.data.repo

import com.movieapp.data.local.FavoriteDao
import com.movieapp.data.local.FavoriteEntity
import com.movieapp.data.remote.NetworkModule
import com.movieapp.data.remote.MovieDto
import com.movieapp.domain.model.Genre
import com.movieapp.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepository(private val favoriteDao: FavoriteDao) {

    private val api = NetworkModule.tmdbApi
    private val apiKey = NetworkModule.apiKey

    suspend fun getTrendingMovies(): Result<List<Movie>> = runCatching {
        api.getTrendingMovies(apiKey).results.map { it.toDomain() }
    }

    suspend fun searchMovies(query: String): Result<List<Movie>> = runCatching {
        api.searchMovies(apiKey, query).results.map { it.toDomain() }
    }

    suspend fun getMovieDetail(id: Int): Result<Movie> = runCatching {
        val dto = api.getMovieDetail(id, apiKey)
        Movie(
            id = dto.id,
            title = dto.title,
            overview = dto.overview,
            posterPath = dto.posterPath,
            backdropPath = dto.backdropPath,
            voteAverage = dto.voteAverage,
            releaseDate = dto.releaseDate,
            genreIds = dto.genres.map { it.id }
        )
    }

    suspend fun getGenres(): Result<List<Genre>> = runCatching {
        api.getGenres(apiKey).genres.map { Genre(it.id, it.name) }
    }

    fun getFavorites(): Flow<List<Movie>> {
        return favoriteDao.getAllFavorites().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    fun isFavorite(movieId: Int): Flow<Boolean> = favoriteDao.isFavorite(movieId)

    suspend fun addFavorite(movie: Movie) {
        favoriteDao.insert(movie.toEntity())
    }

    suspend fun removeFavorite(movieId: Int) {
        favoriteDao.deleteById(movieId)
    }

    private fun MovieDto.toDomain() = Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        voteAverage = voteAverage,
        releaseDate = releaseDate,
        genreIds = genreIds
    )

    private fun FavoriteEntity.toDomain() = Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        voteAverage = voteAverage,
        releaseDate = releaseDate,
        genreIds = genreIds.split(",").mapNotNull { it.toIntOrNull() }
    )

    private fun Movie.toEntity() = FavoriteEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        voteAverage = voteAverage,
        releaseDate = releaseDate,
        genreIds = genreIds.joinToString(",")
    )
}

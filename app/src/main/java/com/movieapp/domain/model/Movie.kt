package com.movieapp.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double,
    val releaseDate: String,
    val genreIds: List<Int>
) {
    val fullPosterPath: String
        get() = "https://image.tmdb.org/t/p/w500${posterPath ?: ""}"

    val fullBackdropPath: String
        get() = "https://image.tmdb.org/t/p/original${backdropPath ?: ""}"
}

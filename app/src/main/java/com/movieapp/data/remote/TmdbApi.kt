package com.movieapp.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("3/trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int = 1
    ): TrendingResponse

    @GET("3/search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int = 1
    ): TrendingResponse

    @GET("3/movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): MovieDetailDto

    @GET("3/genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String
    ): GenreResponse
}

@JsonClass(generateAdapter = true)
data class TrendingResponse(
    @Json(name = "page") val page: Int,
    @Json(name = "results") val results: List<MovieDto>,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name = "total_results") val totalResults: Int
)

@JsonClass(generateAdapter = true)
data class MovieDto(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "overview") val overview: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "release_date") val releaseDate: String,
    @Json(name = "genre_ids") val genreIds: List<Int>
)

@JsonClass(generateAdapter = true)
data class MovieDetailDto(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "overview") val overview: String,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "release_date") val releaseDate: String,
    @Json(name = "genres") val genres: List<GenreDto>,
    @Json(name = "runtime") val runtime: Int?
)

@JsonClass(generateAdapter = true)
data class GenreDto(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String
)

@JsonClass(generateAdapter = true)
data class GenreResponse(
    @Json(name = "genres") val genres: List<GenreDto>
)

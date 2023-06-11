package com.example.movies_mvvm.data.remote_db

import com.example.movies_mvvm.data.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {

    @GET("movie/popular?")
    suspend fun getTrending(@Query("api_key") api_key: String): Response<MoviesResponse>

    @GET("movie/now_playing?")
    suspend fun getInTheaters(@Query("api_key") api_key: String): Response<MoviesResponse>

    @GET("movie/upcoming?")
    suspend fun getUpcoming(@Query("api_key") api_key: String): Response<MoviesResponse>

    @GET("movie/top_rated?")
    suspend fun getTopRated(@Query("api_key") api_key: String): Response<MoviesResponse>

    @GET("genre/movie/list?")
    suspend fun getCategories(@Query("api_key") api_key: String): Response<CategoriesResponse>

    @GET("movie/{movie_id}?")
    suspend fun getMovie(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String
    ): Response<MovieResponse>

    @GET("movie/{movie_id}/casts?")
    suspend fun getCasts(
        @Path("movie_id") movieId: Int,
        @Query("api_key") api_key: String
    ): Response<CastAndCrewResponse>

    @GET("person/{person_id}?")
    suspend fun getCastCrewDetails(
        @Path("person_id") personId: Int,
        @Query("api_key") api_key: String
    ): Response<CastCrewDetailsResponse>

    @GET("movie/{movie_id}/recommendations?")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("api_key") api_key: String
    ): Response<SimilarMoviesResponse>

    @GET("search/movie?")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") api_key: String
    ): Response<SearchResultResponse>

}
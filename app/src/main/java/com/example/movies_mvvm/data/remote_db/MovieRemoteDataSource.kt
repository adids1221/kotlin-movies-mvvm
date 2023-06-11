package com.example.movies_mvvm.data.remote_db

import com.example.movies_mvvm.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRemoteDataSource @Inject constructor(private val moviesService: MoviesService) :
    BaseDataSource() {
    private val API_KEY: String = BuildConfig.API_KEY
    suspend fun getTrending() = getResults { moviesService.getTrending(API_KEY) }
    suspend fun getInTheaters() = getResults { moviesService.getInTheaters(API_KEY) }
    suspend fun getUpcoming() = getResults { moviesService.getUpcoming(API_KEY) }
    suspend fun getTopRated() = getResults { moviesService.getTopRated(API_KEY) }
    suspend fun getCategories() = getResults { moviesService.getCategories(API_KEY) }
    suspend fun getMovie(movie_id: Int) = getResults { moviesService.getMovie(movie_id, API_KEY) }
    suspend fun getCasts(movie_id: Int) = getResults { moviesService.getCasts(movie_id, API_KEY) }
    suspend fun getCastCrewDetails(person_id: Int) =
        getResults { moviesService.getCastCrewDetails(person_id, API_KEY) }

    suspend fun getSimilarMovies(movie_id: Int) =
        getResults { moviesService.getSimilarMovies(movie_id, API_KEY) }

    suspend fun searchMovies(query: String) =
        getResults { moviesService.searchMovies(query, API_KEY) }
}
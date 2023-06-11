package com.example.movies_mvvm.data.repository

import android.app.Application

import com.example.movies_mvvm.data.local_db.MovieDao
import com.example.movies_mvvm.data.local_db.MovieDataBase
import com.example.movies_mvvm.data.model.Movie
import com.example.movies_mvvm.data.remote_db.MovieRemoteDataSource
import utils.performFetchingAndSaving
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val remoteDataSource: MovieRemoteDataSource,
    private val localDataSource: MovieDao
) {
    fun getTrending() = performFetchingAndSaving({ localDataSource.getAllTrending() },
        { remoteDataSource.getTrending() }, { localDataSource.insertTrending(it.results) })
}

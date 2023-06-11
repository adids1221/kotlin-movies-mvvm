package com.example.movies_mvvm.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.movies_mvvm.data.model.Movie

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM movies WHERE type = :type")
    fun getAllTrending(type: Int? = 0): LiveData<List<Movie>>

    @Query("SELECT * FROM movies WHERE type = :type")
    fun getAllUpcoming(type: Int = 1): List<Movie>

    @Query("SELECT * FROM movies WHERE type = :type")
    fun getAllInTheaters(type: Int = 2): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrending(movies: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpcoming(movies: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInTheaters(movies: List<Movie>)

//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun addMovie(movie: Movie)
//
//    @Delete
//    suspend fun removeMovie(vararg movies: Movie)
//
//    @Update
//    suspend fun updateMovie(movie: Movie)
//
//    @Query("SELECT * from movies_table ORDER BY title ASC")
//    fun getAllMovies(): LiveData<List<Movie>>
//
//
//    @Query("DELETE from movies_table")
//    suspend fun removeAll()
}
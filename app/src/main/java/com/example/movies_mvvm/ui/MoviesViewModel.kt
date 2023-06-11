package com.example.movies_mvvm.ui

import androidx.lifecycle.*
import com.example.movies_mvvm.data.model.Movie
import com.example.movies_mvvm.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor() : ViewModel() {

//    private val repository = MovieRepository(application)
//
//    val items: LiveData<List<Movie>>? = repository.getItems()
//
//    private val _chosenItem = MutableLiveData<Movie>()
//    val chosenItem: LiveData<Movie> get() = _chosenItem
//
//    fun setItem(movie: Movie) {
//        _chosenItem.value = movie
//    }
//
//    fun addItem(movie: Movie) {
//        viewModelScope.launch {
//            repository.addItem(movie)
//        }
//    }
//
//    fun updateItem(movie: Movie) {
//        viewModelScope.launch {
//            repository.updateItem(movie)
//        }
//    }
//
//    fun removeItem(movie: Movie) {
//        viewModelScope.launch {
//            repository.removeItem(movie)
//        }
//    }
//
//    fun removeAll() {
//        viewModelScope.launch {
//            repository.removeAll()
//        }
//    }
}
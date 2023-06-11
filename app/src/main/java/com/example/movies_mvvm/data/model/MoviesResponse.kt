package com.example.movies_mvvm.data.model

data class MoviesResponse(
    var page: Int? = null,
    var results: List<Movie> = listOf(),
    var totalPages: Int? = null,
    var totalResults: Int? = null
) {}
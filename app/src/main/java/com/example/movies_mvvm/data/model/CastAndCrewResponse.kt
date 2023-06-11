package com.example.movies_mvvm.data.model

data class CastAndCrewResponse(
    var id: Int? = null,
    var cast: ArrayList<Cast> = arrayListOf(),
    var crew: ArrayList<Crew> = arrayListOf()
) {
}
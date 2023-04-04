package com.example.movies_mvvm

data class Item(
    val title: String,
    val description: String,
    val releaseDate: String,
    val rating: Double,
    val poster: String
)

object MovieItemManager {
    val items: MutableList<Item> = mutableListOf()

    fun add(movieItem: Item) {
        items.add(movieItem)
    }

    fun remove(position: Int) {
        items.removeAt(position)
    }
}

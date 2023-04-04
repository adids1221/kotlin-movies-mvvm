package com.example.movies_mvvm.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.movies_mvvm.data.model.Item
import com.example.movies_mvvm.data.repository.ItemRepository

class ItemsViewModel(application: Application):AndroidViewModel(application) {

    private val repository = ItemRepository(application)

    val items : LiveData<List<Item>>? = repository.getItems()

    fun addItem(item: Item){
        repository.addItem(item)
    }
    fun removeItem(item: Item){
        repository.removeItem(item)
    }
}
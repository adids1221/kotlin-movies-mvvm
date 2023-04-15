package com.example.movies_mvvm.data.repository

import android.app.Application

import com.example.movies_mvvm.data.local_db.ItemDao
import com.example.movies_mvvm.data.local_db.ItemDataBase
import com.example.movies_mvvm.data.model.Item

class ItemRepository(application: Application) {

    private var itemDao: ItemDao?

    init {
        val db = ItemDataBase.getDataBase(application.applicationContext)
        itemDao = db.itemsDao()
    }

    fun getItemByTitle(title: String) = itemDao?.getItemByTitle(title)

    fun getItems() = itemDao?.getItems()

    suspend fun addItem(item: Item) {
        itemDao?.addItem(item)
    }

    suspend fun updateItem(item: Item) {
        itemDao?.updateItem(item)
    }

    suspend fun removeItem(item: Item) {
        itemDao?.removeItem(item)
    }

    suspend fun removeAll() {
        itemDao?.removeAll()
    }

}

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

    fun addItem(item: Item) {
        itemDao?.addItem(item)
    }

    fun updateItem(item: Item) {
        itemDao?.updateItem(item)
    }

    fun removeItem(item: Item) {
        itemDao?.removeItem(item)
    }

    fun removeAll() {
        itemDao?.removeAll()
    }

}

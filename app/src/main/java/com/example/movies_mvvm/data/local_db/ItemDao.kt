package com.example.movies_mvvm.data.local_db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.movies_mvvm.data.model.Item

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItem(item: Item)

    @Delete
    fun removeItem(vararg items: Item)

    @Update
    fun updateItem(item: Item)

    @Query("SELECT * from movies_table ORDER BY title ASC")
    fun getItems(): LiveData<List<Item>>

    @Query("SELECT * from movies_table WHERE title =:title")
    fun getItemByTitle(title: String?): Item
}
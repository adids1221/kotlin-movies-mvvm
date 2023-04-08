package com.example.movies_mvvm.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "movies_table")
data class Item(
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "release_date")
    val releaseDate: String,
    @ColumnInfo(name = "rating")
    val rating: Double?,
    @ColumnInfo(name = "poster")
    val poster: String?
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

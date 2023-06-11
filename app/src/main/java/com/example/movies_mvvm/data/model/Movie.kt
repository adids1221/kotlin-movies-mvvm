package com.example.movies_mvvm.data.model

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

class GenreIdsConverter {
    @TypeConverter
    fun fromList(list: ArrayList<Int>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toList(data: String): ArrayList<Int> {
        return ArrayList(data.split(",").map { it.toInt() })
    }
}


@Parcelize
@Entity(tableName = "movies_table", primaryKeys = ["id", "type"])
@TypeConverters(GenreIdsConverter::class)
data class Movie(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int? = null,
    @ColumnInfo("adult")
    val adult: Boolean? = null,
    @ColumnInfo("backdrop_path")
    val backdropPath: String? = null,
    @ColumnInfo("genre_ids")
    val genreIds: ArrayList<Int> = arrayListOf(),
    @ColumnInfo("original_language")
    val originalLanguage: String? = null,
    @ColumnInfo("original_title")
    val originalTitle: String? = null,
    @ColumnInfo("poster_path")
    val posterPath: String? = null,
    @ColumnInfo("title")
    val title: String? = null,
    @ColumnInfo("video")
    val video: Boolean? = null,
    @ColumnInfo("vote_average")
    val voteAverage: Double? = null,
    @ColumnInfo("overview")
    val overview: String? = null,
    @ColumnInfo("release_date")
    val releaseDate: String? = null,
    @ColumnInfo("vote_count")
    val voteCount: Int? = null,
    @ColumnInfo("popularity")
    val popularity: Double? = null,
    @ColumnInfo("character")
    val character: String? = null,
    @ColumnInfo("credit_id")
    val creditId: String? = null,
    @ColumnInfo("order")
    val order: Int? = null,
    var type: Int = 0,
) : Parcelable

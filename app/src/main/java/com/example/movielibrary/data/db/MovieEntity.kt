package com.example.movielibrary.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representación de una película en la base de datos.
 * Esta clase SÍ conoce a Room (por las anotaciones).
 */
@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "year")
    val year: Int,

    @ColumnInfo(name = "genre")
    val genre: String,

    @ColumnInfo(name = "rating")
    val rating: Float,

    @ColumnInfo(name = "watched")
    val watched: Boolean
)
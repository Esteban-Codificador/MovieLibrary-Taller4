package com.example.movielibrary.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * Data Access Object. Define las operaciones que se pueden hacer
 * sobre la tabla "movies". Room genera la implementación automáticamente.
 */
@Dao
interface MovieDao {
    @Query("SELECT * FROM movies ORDER BY title ASC")
    fun getAllMovies(): LiveData<List<MovieEntity>>

    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    fun getMovieById(id: Int): LiveData<MovieEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: MovieEntity): Long

    @Update
    suspend fun update(movie: MovieEntity)

    @Delete
    suspend fun delete(movie: MovieEntity)
}
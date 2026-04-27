package com.example.movielibrary.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.movielibrary.data.db.MovieDao
import com.example.movielibrary.data.toDomain
import com.example.movielibrary.data.toDomainList
import com.example.movielibrary.data.toEntity
import com.example.movielibrary.model.Movie

/**
 * Repository: única fuente de verdad para los datos de películas.
 * El ViewModel NO accede a Room directamente; siempre pasa por aquí.
 *
 * Internamente trabaja con MovieEntity (Room), pero expone Movie (dominio).
 * Las conversiones se hacen con las funciones del MovieMapper.
 */
class MovieRepository(private val dao: MovieDao) {

    fun getMovies(): LiveData<List<Movie>> {
        // map() transforma el LiveData<List<MovieEntity>> en LiveData<List<Movie>>
        return dao.getAllMovies().map { entities -> entities.toDomainList() }
    }

    fun getMovie(id: Int): LiveData<Movie?> {
        return dao.getMovieById(id).map { entity -> entity?.toDomain() }
    }
    suspend fun insert(movie: Movie) {
        dao.insert(movie.toEntity())
    }

    suspend fun update(movie: Movie) {
        dao.update(movie.toEntity())
    }

    suspend fun delete(movie: Movie) {
        dao.delete(movie.toEntity())
    }
}
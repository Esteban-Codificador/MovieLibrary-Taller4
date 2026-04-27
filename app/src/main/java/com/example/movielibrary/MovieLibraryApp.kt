package com.example.movielibrary

import android.app.Application
import com.example.movielibrary.data.db.AppDatabase
import com.example.movielibrary.repository.MovieRepository

/**
 * Clase Application personalizada. Crea una sola instancia de la base de datos
 * y del repository, accesibles desde cualquier Fragment vía requireActivity().application.
 */
class MovieLibraryApp : Application() {

    // by lazy: se inicializa la primera vez que se usa, no antes.
    private val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { MovieRepository(database.movieDao()) }
}
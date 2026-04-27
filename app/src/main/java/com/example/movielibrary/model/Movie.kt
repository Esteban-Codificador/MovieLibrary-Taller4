package com.example.movielibrary.model

/**
 * Clase de dominio. Representa una película en la lógica de la app,
 * sin conocer detalles de Room. Los Fragments y el ViewModel trabajan con esta clase.
 */
data class Movie(
    val id: Int = 0,
    val title: String,
    val year: Int,
    val genre: String,
    val rating: Float,
    val watched: Boolean = false
)
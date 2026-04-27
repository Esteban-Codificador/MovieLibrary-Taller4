package com.example.movielibrary.data

import com.example.movielibrary.data.db.MovieEntity
import com.example.movielibrary.model.Movie

/**
 * Funciones de extensión para convertir entre la entidad de Room (MovieEntity)
 * y el modelo de dominio (Movie). Mantienen aislada la capa de datos del resto de la app.
 */

fun MovieEntity.toDomain(): Movie = Movie(
    id = id,
    title = title,
    year = year,
    genre = genre,
    rating = rating,
    watched = watched
)

fun Movie.toEntity(): MovieEntity = MovieEntity(
    id = id,
    title = title,
    year = year,
    genre = genre,
    rating = rating,
    watched = watched
)

fun List<MovieEntity>.toDomainList(): List<Movie> = this.map { it.toDomain() }
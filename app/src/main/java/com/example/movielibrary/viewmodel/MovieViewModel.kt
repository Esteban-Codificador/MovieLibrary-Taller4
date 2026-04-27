package com.example.movielibrary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielibrary.model.Movie
import com.example.movielibrary.repository.MovieRepository
import kotlinx.coroutines.launch

/**
 * ViewModel: mantiene el estado de la UI y sobrevive a cambios de configuración
 * (rotaciones, etc.). Expone los datos como LiveData para que los Fragments
 * los observen y se actualicen automáticamente.
 *
 * Las operaciones de escritura usan viewModelScope para no bloquear el hilo principal.
 */
class MovieViewModel(private val repository: MovieRepository) : ViewModel() {

    /**
     * Lista completa de películas. La UI la observa para mostrar el RecyclerView.
     * Cuando se inserte/actualice/borre una película, Room emitirá un nuevo valor
     * y la lista se refrescará automáticamente.
     */
    val movies: LiveData<List<Movie>> = repository.getMovies()

    /**
     * Devuelve una película específica como LiveData.
     * Se usa en el fragmento de detalle para observar cambios sobre esa película.
     */
    fun getMovie(id: Int): LiveData<Movie?> = repository.getMovie(id)

    fun insert(movie: Movie) {
        viewModelScope.launch {
            repository.insert(movie)
        }
    }

    fun update(movie: Movie) {
        viewModelScope.launch {
            repository.update(movie)
        }
    }

    fun delete(movie: Movie) {
        viewModelScope.launch {
            repository.delete(movie)
        }
    }

    /**
     * Cambia el estado watched de una película (vista / no vista).
     * Útil para el botón de toggle en el fragmento de detalle.
     */
    fun toggleWatched(movie: Movie) {
        viewModelScope.launch {
            repository.update(movie.copy(watched = !movie.watched))
        }
    }
}
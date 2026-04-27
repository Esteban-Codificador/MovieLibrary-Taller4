package com.example.movielibrary.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movielibrary.databinding.ItemMovieBinding
import com.example.movielibrary.model.Movie

/**
 * Adapter del RecyclerView que muestra la lista de películas.
 * Usa ListAdapter + DiffUtil para que solo se redibujen las filas que cambiaron.
 */
class MovieAdapter(
    private val onMovieClick: (Movie) -> Unit
) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(DIFF_CALLBACK) {

    inner class MovieViewHolder(
        private val binding: ItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.tvTitle.text = movie.title
            binding.tvSubtitle.text = "${movie.year} · ${movie.genre} · ★ ${movie.rating}"
            binding.ivWatched.visibility = if (movie.watched) {
                android.view.View.VISIBLE
            } else {
                android.view.View.GONE
            }
            binding.root.setOnClickListener { onMovieClick(movie) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }
}
package com.example.movielibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.movielibrary.MovieLibraryApp
import com.example.movielibrary.databinding.FragmentMovieDetailBinding
import com.example.movielibrary.model.Movie
import com.example.movielibrary.viewmodel.MovieViewModel
import com.example.movielibrary.viewmodel.MovieViewModelFactory
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!
    private val args: MovieDetailFragmentArgs by navArgs()
    private val viewModel: MovieViewModel by viewModels {
        val app = requireActivity().application as MovieLibraryApp
        MovieViewModelFactory(app.repository)
    }

    private var currentMovie: Movie? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = args.movieId

        if (movieId == -1) {
            return
        }

        // Observar la película específica
        viewModel.getMovie(movieId).observe(viewLifecycleOwner) { movie ->
            if (movie == null) {
                binding.tvTitle.text = "Película no encontrada"
                return@observe
            }
            currentMovie = movie
            renderMovie(movie)
        }

        binding.btnToggleWatched.setOnClickListener {
            currentMovie?.let { viewModel.toggleWatched(it) }
        }

        binding.btnEdit.setOnClickListener {
            // FASE B: Safe Args
            val action = MovieDetailFragmentDirections
                .actionDetailToEdit(movieId = movieId)
            findNavController().navigate(action)
        }

        binding.btnDelete.setOnClickListener {
            currentMovie?.let {
                viewModel.delete(it)
                // TODO: Parte 5 — volver a la lista con NavController
            }
        }
    }

    private fun renderMovie(movie: Movie) {
        binding.tvTitle.text = movie.title
        binding.tvYear.text = "Año: ${movie.year}"
        binding.tvGenre.text = "Género: ${movie.genre}"
        binding.tvRating.text = "Rating: ${movie.rating} / 5"
        binding.tvWatched.text = if (movie.watched) "Estado: Vista" else "Estado: No vista"
        binding.btnToggleWatched.text =
            if (movie.watched) "Marcar como no vista" else "Marcar como vista"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
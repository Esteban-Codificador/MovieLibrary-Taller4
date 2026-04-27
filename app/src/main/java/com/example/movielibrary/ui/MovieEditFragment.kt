package com.example.movielibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.movielibrary.MovieLibraryApp
import com.example.movielibrary.databinding.FragmentMovieEditBinding
import com.example.movielibrary.model.Movie
import com.example.movielibrary.viewmodel.MovieViewModel
import com.example.movielibrary.viewmodel.MovieViewModelFactory
class MovieEditFragment : Fragment() {

    private var _binding: FragmentMovieEditBinding? = null
    private val binding get() = _binding!!
    private val args: MovieEditFragmentArgs by navArgs()
    private val viewModel: MovieViewModel by viewModels {
        val app = requireActivity().application as MovieLibraryApp
        MovieViewModelFactory(app.repository)
    }

    private var editingMovie: Movie? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = args.movieId

        if (movieId != -1) {
            // Modo editar: cargar los datos
            binding.tvHeader.text = "Editar película"
            viewModel.getMovie(movieId).observe(viewLifecycleOwner) { movie ->
                if (movie != null && editingMovie == null) {
                    editingMovie = movie
                    binding.etTitle.setText(movie.title)
                    binding.etYear.setText(movie.year.toString())
                    binding.etGenre.setText(movie.genre)
                    binding.etRating.setText(movie.rating.toString())
                    binding.cbWatched.isChecked = movie.watched
                }
            }
        }

        binding.btnSave.setOnClickListener { onSave() }
    }

    private fun onSave() {
        val title = binding.etTitle.text.toString().trim()
        val yearStr = binding.etYear.text.toString().trim()
        val genre = binding.etGenre.text.toString().trim()
        val ratingStr = binding.etRating.text.toString().trim()
        val watched = binding.cbWatched.isChecked

        if (title.isEmpty() || yearStr.isEmpty() || genre.isEmpty() || ratingStr.isEmpty()) {
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val year = yearStr.toIntOrNull()
        val rating = ratingStr.toFloatOrNull()
        if (year == null || rating == null) {
            Toast.makeText(requireContext(), "Año o rating inválidos", Toast.LENGTH_SHORT).show()
            return
        }

        val existing = editingMovie
        if (existing == null) {
            // Crear nueva
            viewModel.insert(Movie(
                title = title,
                year = year,
                genre = genre,
                rating = rating,
                watched = watched
            ))
            Toast.makeText(requireContext(), "Película creada", Toast.LENGTH_SHORT).show()
        } else {
            // Actualizar existente
            viewModel.update(existing.copy(
                title = title,
                year = year,
                genre = genre,
                rating = rating,
                watched = watched
            ))
            Toast.makeText(requireContext(), "Película actualizada", Toast.LENGTH_SHORT).show()
        }

        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
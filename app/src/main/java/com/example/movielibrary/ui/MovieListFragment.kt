package com.example.movielibrary.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movielibrary.MovieLibraryApp
import com.example.movielibrary.databinding.FragmentMovieListBinding
import com.example.movielibrary.viewmodel.MovieViewModel
import com.example.movielibrary.viewmodel.MovieViewModelFactory

class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieViewModel by viewModels {
        val app = requireActivity().application as MovieLibraryApp
        MovieViewModelFactory(app.repository)
    }

    private lateinit var adapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar RecyclerView
        adapter = MovieAdapter { movie ->
            // FASE B: Safe Args
            val action = MovieListFragmentDirections
                .actionListToDetail(movieId = movie.id)
            findNavController().navigate(action)
        }
        binding.recyclerMovies.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMovies.adapter = adapter

        // Observar la lista de películas (LiveData)
        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            adapter.submitList(movies)
            binding.tvCount.text = "${movies.size} película(s)"
            binding.tvEmpty.visibility = if (movies.isEmpty()) View.VISIBLE else View.GONE
        }

        // FAB: agregar película
        binding.fabAdd.setOnClickListener {
            // FASE B: Safe Args (sin movieId → modo crear)
            val action = MovieListFragmentDirections.actionListToEdit()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
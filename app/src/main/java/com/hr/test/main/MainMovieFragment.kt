package com.hr.test.main

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hr.core.viewmodel.ContentMovieViewStateData
import com.hr.core.viewmodel.MoviesViewModel
import com.hr.core.viewmodel.MoviesViewState
import com.hr.test.NavigationGraphMainDirections

abstract class MainMovieFragment : Fragment() {

    private lateinit var viewModel: MoviesViewModel

    protected val moviesAdapter: MoviesAdapter by lazy {
        MoviesAdapter(
            itemLayoutId(),
            { movieName, liked -> viewModel.updateLike(movieName, liked) },
            { movieName -> navigateToDetailScreen(movieName) }
        )
    }

    @LayoutRes
    abstract fun itemLayoutId(): Int

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun navigateToDetailScreen(movieName: String) {
        val action = NavigationGraphMainDirections.actionGlobalMovieDetailFragment(movieName)
        findNavController().navigate(action)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        viewModel.observeMovies().observe(viewLifecycleOwner, Observer {
            if (it is MoviesViewState.Content) {
                renderContent(it.movieList)
            }
        })
    }

    private fun renderContent(movieList: List<ContentMovieViewStateData>) {
        moviesAdapter.setMovies(movieList)
    }
}
package com.hr.test.main

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hr.core.viewmodel.ContentMovieViewStateData
import com.hr.core.viewmodel.MoviesViewModel
import com.hr.core.viewmodel.MoviesViewState
import com.hr.test.NavigationGraphMainDirections
import kotlinx.android.synthetic.main.main_activity.*

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

    private fun navigateToDetailScreen(movieName: String) {
        val action = NavigationGraphMainDirections.actionGlobalMovieDetailFragment(movieName)
        findNavController().navigate(action)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        viewModel.observeMovies().observe(viewLifecycleOwner, Observer {
            renderState(it)
        })
    }

    private fun renderState(viewState: MoviesViewState) {
        when (viewState) {
            MoviesViewState.Loading -> {
                renderLoading()
            }
            is MoviesViewState.Content -> {
                renderContent(viewState.movieList)
            }
            else -> {
                // no-op
            }
        }
    }

    private fun renderLoading() {
        contentViewAnimator.displayedChild = LOADING_INDEX
    }

    private fun renderContent(movieList: List<ContentMovieViewStateData>) {
        contentViewAnimator.displayedChild = CONTENT_INDEX
        moviesAdapter.setMovies(movieList)
    }
}

private const val LOADING_INDEX = 0
private const val CONTENT_INDEX = 1
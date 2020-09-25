package com.hr.test.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hr.core.viewmodel.ContentMovieViewStateData
import com.hr.core.viewmodel.MoviesViewModel
import com.hr.core.viewmodel.MoviesViewState
import com.hr.test.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main_layout.*
import kotlinx.android.synthetic.main.main_content_view.*
import kotlinx.android.synthetic.main.main_error_view.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MoviesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_layout)

        viewModel = ViewModelProvider(this).get(MoviesViewModel::class.java)
        viewModel.fetch()
        viewModel.observeMovies().observe(this, Observer {
            renderState(it)
        })
    }

    private fun renderState(viewState: MoviesViewState) = when (viewState) {
        MoviesViewState.Loading -> {
            renderLoading()
        }
        is MoviesViewState.Content -> {
            renderContent(viewState.movieList)
        }
        MoviesViewState.Error -> {
            renderError()
        }
    }

    private fun renderLoading() {
        contentViewAnimator.displayedChild = LOADING_INDEX
    }

    private fun renderContent(campaignModelList: List<ContentMovieViewStateData>) {
        contentViewAnimator.displayedChild = CONTENT_INDEX
        contentMovieListRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = MoviesAdapter { movieName, liked ->
                viewModel.updateLike(movieName, liked)
            }.apply { setMovies(campaignModelList) }
        }
    }

    private fun renderError() {
        contentViewAnimator.displayedChild = ERROR_INDEX
        retryButton.setOnClickListener {
            renderLoading()
            viewModel.fetch()
        }
    }
}

private const val LOADING_INDEX = 0
private const val CONTENT_INDEX = 1
private const val ERROR_INDEX = 2
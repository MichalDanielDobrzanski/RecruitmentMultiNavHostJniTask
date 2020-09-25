package com.hr.core.viewmodel

import com.hr.models.Movie

/**
 * ViewState to be used in view state pattern only for rendering state in view later
 */
sealed class MoviesViewState {

    object Loading : MoviesViewState()

    data class Content(val movieList: List<ContentMovieViewStateData>) : MoviesViewState()

    object Error : MoviesViewState()
}

data class ContentMovieViewStateData(
    val movie: Movie,
    val liked: Boolean
)
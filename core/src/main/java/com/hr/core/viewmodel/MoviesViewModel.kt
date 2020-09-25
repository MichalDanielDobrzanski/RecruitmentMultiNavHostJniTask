package com.hr.core.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.hr.core.usecase.GetMoviesUseCase

class MoviesViewModel @ViewModelInject constructor(
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    fun observeMovies(): LiveData<MoviesViewState> = LiveDataReactiveStreams.fromPublisher(getMoviesUseCase.moviesViewStateStream)

    fun fetch() = getMoviesUseCase.fetch()

    fun updateLike(movieName: String, liked: Boolean) = getMoviesUseCase.updateLike(movieName, liked)
}
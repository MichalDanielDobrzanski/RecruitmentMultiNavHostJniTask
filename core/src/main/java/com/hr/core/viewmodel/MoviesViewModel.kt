package com.hr.core.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.hr.core.repository.MoviesRepository
import com.hr.core.repository.MoviesRepositoryModel
import io.reactivex.Flowable


class MoviesViewModel @ViewModelInject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val moviesViewStateStream: Flowable<MoviesViewState>
        get() = moviesRepository.moviesStream()
            .map<MoviesViewState> { model ->
                return@map when (model) {
                    is MoviesRepositoryModel.Data -> MoviesViewState.Content(model.movieList)
                    MoviesRepositoryModel.NotPresent -> MoviesViewState.Loading
                    MoviesRepositoryModel.Error -> MoviesViewState.Error
                }
            }


    fun fetch(): LiveData<MoviesViewState> = LiveDataReactiveStreams.fromPublisher(moviesViewStateStream)
}
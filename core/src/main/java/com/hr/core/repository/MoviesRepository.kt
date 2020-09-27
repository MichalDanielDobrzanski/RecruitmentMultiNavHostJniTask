package com.hr.core.repository

import com.hr.core.repository.models.MoviesRepositoryModel
import com.hr.models.MovieDetail
import io.reactivex.Flowable

interface MoviesRepository {
    fun fetchMovies()

    fun moviesStream(): Flowable<MoviesRepositoryModel>

    fun movieDetailsStream(movieName: String): Flowable<MovieDetail>
}
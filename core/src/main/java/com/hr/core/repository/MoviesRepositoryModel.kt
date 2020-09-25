package com.hr.core.repository

import com.hr.models.Movie

sealed class MoviesRepositoryModel {
    data class Data(val movieList: List<Movie>) : MoviesRepositoryModel()

    object NotPresent : MoviesRepositoryModel()

    object Error : MoviesRepositoryModel()
}
package com.hr.core.viewmodel

import com.hr.models.MovieDetail

data class MovieDetailViewState(
    val movieDetail: MovieDetail,
    val liked: Boolean
)
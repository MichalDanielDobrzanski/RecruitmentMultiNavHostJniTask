package com.hr.models

data class MovieDetail(
    val movieName: String,
    val score: Float,
    val actors: List<Actor>,
    val description: String
)
package com.hr.core.repository


interface LikedMoviesCache {

    fun updateLike(movieName: String, liked: Boolean)

    fun getState(): LikedMovies

    fun isLiked(movieName: String): Boolean
}

/**
 * Simple in-memory cached app-scoped data
 */
class LikedMoviesCacheImpl : LikedMoviesCache {

    private val likedMoviesMap: MutableMap<String, Boolean> = HashMap()

    override fun updateLike(movieName: String, liked: Boolean) {
        likedMoviesMap[movieName] = liked
    }

    override fun getState(): LikedMovies = likedMoviesMap

    override fun isLiked(movieName: String): Boolean = likedMoviesMap[movieName] ?: false
}


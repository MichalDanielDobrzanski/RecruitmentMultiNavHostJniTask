package com.hr.core.repository


interface LikedMoviesRepository {

    fun updateLike(movieName: String, liked: Boolean)

    fun getState(): Map<String, Boolean>
}

/**
 * Simple in-memory cached app-scoped data
 */
class LikedMoviesRepositoryImpl : LikedMoviesRepository {

    private val likedMoviesMap: MutableMap<String, Boolean> = HashMap()

    override fun updateLike(movieName: String, liked: Boolean) {
        likedMoviesMap[movieName] = liked
    }

    override fun getState(): Map<String, Boolean> = likedMoviesMap
}


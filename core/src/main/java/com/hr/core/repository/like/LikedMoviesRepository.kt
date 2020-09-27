package com.hr.core.repository.like

import io.reactivex.Flowable
import io.reactivex.processors.BehaviorProcessor
import javax.inject.Inject

typealias LikedMovies = Map<String, Boolean>

interface LikedMoviesRepository {

    fun likedMoviesStream(): Flowable<LikedMovies>

    fun fetch()

    fun updateLike(movieName: String, liked: Boolean)
}

class LikedMoviesRepositoryImpl @Inject constructor(
    private val likedMoviesCache: LikedMoviesCache
) : LikedMoviesRepository {

    private val moviesProcessor: BehaviorProcessor<LikedMovies> = BehaviorProcessor.createDefault(likedMoviesCache.getState())

    override fun likedMoviesStream(): Flowable<LikedMovies> = moviesProcessor.onBackpressureLatest()

    override fun fetch() {
        moviesProcessor.onNext(likedMoviesCache.getState())
    }

    override fun updateLike(movieName: String, liked: Boolean) {
        likedMoviesCache.updateLike(movieName, liked)
        moviesProcessor.onNext(likedMoviesCache.getState())
    }
}
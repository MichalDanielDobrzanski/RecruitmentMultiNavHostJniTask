package com.hr.core.repository

import com.hr.models.Movie
import com.hr.models.Name
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

interface MoviesRepository {
    fun moviesStream(): Flowable<MoviesRepositoryModel>
}

class DummyMoviesRepositoryThatShouldBeReplaced : MoviesRepository {
    override fun moviesStream(): Flowable<MoviesRepositoryModel> = Flowable
        .timer(2L, TimeUnit.SECONDS)
        .map { _ ->
            MoviesRepositoryModel.Data(0.until(20).map {
                Movie(Name("Dummy movie title $it"))
            })
        }
}
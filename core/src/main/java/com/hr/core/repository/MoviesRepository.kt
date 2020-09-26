package com.hr.core.repository

import com.hr.models.Actor
import com.hr.models.Movie
import com.hr.models.MovieDetail
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import java.util.concurrent.TimeUnit

interface MoviesRepository {
    fun fetchMovies()

    fun moviesStream(): Flowable<MoviesRepositoryModel>

    fun movieDetailsSingle(movieName: String): Single<MovieDetail>
}

class DummyMoviesRepositoryThatShouldBeReplaced : MoviesRepository {

    private val moviesProcessor: BehaviorProcessor<MoviesRepositoryModel> = BehaviorProcessor.createDefault(MoviesRepositoryModel.NotPresent)

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val dummyMoviesStream: Flowable<MoviesRepositoryModel>
        get() = Flowable
            .timer(2L, TimeUnit.SECONDS)
            .map { _ ->
                MoviesRepositoryModel.Data(
                    0.until(20)
                        .map { Movie("Dummy movie title $it", 0) }
                        .toMutableList()
                        .apply { add(0, Movie("Some name", 1)) }
                )
            }

    override fun fetchMovies() {
        compositeDisposable.clear()
        compositeDisposable.add(
            dummyMoviesStream
                .onErrorReturnItem(MoviesRepositoryModel.Error)
                .subscribe { moviesProcessor.onNext(it) }
        )
    }

    override fun moviesStream(): Flowable<MoviesRepositoryModel> = moviesProcessor.onBackpressureLatest()

    override fun movieDetailsSingle(movieName: String) = Single.just(
        MovieDetail(
            "Some name",
            4.5f,
            listOf(
                Actor(
                    "Jennifer Connelly",
                    39,
                    "https://m.media-amazon.com/images/M/MV5BOTczNTgzODYyMF5BMl5BanBnXkFtZTcwNjk4ODk4Mw@@._V1_UY317_CR12,0,214,317_AL_.jpg"
                )
            ),
            "Very cool film. I recomment it"
        )
    )
}
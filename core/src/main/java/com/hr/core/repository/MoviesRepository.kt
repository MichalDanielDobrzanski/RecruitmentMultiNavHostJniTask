package com.hr.core.repository

import com.hr.models.Movie
import com.hr.models.Name
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import java.util.concurrent.TimeUnit

interface MoviesRepository {
    fun moviesStream(): Flowable<MoviesRepositoryModel>
    fun fetch()
}

class DummyMoviesRepositoryThatShouldBeReplaced : MoviesRepository {

    private val moviesProcessor: BehaviorProcessor<MoviesRepositoryModel> = BehaviorProcessor.createDefault(MoviesRepositoryModel.NotPresent)

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val dummyMoviesStream: Flowable<MoviesRepositoryModel>
        get() = Flowable
            .timer(2L, TimeUnit.SECONDS)
            .map { _ ->
                MoviesRepositoryModel.Data(0.until(20).map {
                    Movie(Name("Dummy movie title $it"))
                })
            }

    override fun fetch() {
        compositeDisposable.clear()
        compositeDisposable.add(
            dummyMoviesStream
                .onErrorReturnItem(MoviesRepositoryModel.Error)
                .subscribe { moviesProcessor.onNext(it) }
        )
    }

    override fun moviesStream(): Flowable<MoviesRepositoryModel> = moviesProcessor.onBackpressureLatest()


}
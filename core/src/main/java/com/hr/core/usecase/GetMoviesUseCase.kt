package com.hr.core.usecase

import com.hr.core.repository.*
import com.hr.core.viewmodel.ContentMovieViewStateData
import com.hr.core.viewmodel.MovieDetailViewState
import com.hr.core.viewmodel.MoviesViewState
import com.hr.models.Movie
import com.hr.models.MovieDetail
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val likedMoviesRepository: LikedMoviesRepository
) {
    val moviesViewStateStream: Flowable<MoviesViewState>
        get() = Flowable.combineLatest(
            moviesRepository.moviesStream(),
            likedMoviesRepository.likedMoviesStream(),
            BiFunction { model: MoviesRepositoryModel, likedMovies: LikedMovies ->
                return@BiFunction when (model) {
                    is MoviesRepositoryModel.Data -> mergeWithLikedMovies(model, likedMovies)
                    MoviesRepositoryModel.NotPresent -> MoviesViewState.Loading
                    MoviesRepositoryModel.Error -> MoviesViewState.Error
                }
            })

    fun fetch() = moviesRepository.fetchMovies()

    private fun mergeWithLikedMovies(model: MoviesRepositoryModel.Data, likedMovies: LikedMovies): MoviesViewState =
        MoviesViewState.Content(
            model.movieList.map { movie -> ContentMovieViewStateData(movie, isLiked(likedMovies, movie.name)) }
        )

    fun updateLike(movieName: String, liked: Boolean) = likedMoviesRepository.updateLike(movieName, liked)

    private fun isLiked(likedMovies: LikedMovies, movieName: String) = likedMovies[movieName] ?: false

    fun fetchMoviesDetails(movieName: String): Flowable<MovieDetailViewState> =
        Flowable.combineLatest(
            moviesRepository.movieDetailsSingle(movieName).toFlowable(),
            likedMoviesRepository.likedMoviesStream(),
            BiFunction { model: MovieDetail, likedMovies: LikedMovies ->
                MovieDetailViewState(model, isLiked(likedMovies, model.movieName))
            })

}
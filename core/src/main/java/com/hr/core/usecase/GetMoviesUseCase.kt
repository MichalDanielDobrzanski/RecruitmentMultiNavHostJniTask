package com.hr.core.usecase

import com.hr.core.repository.LikedMoviesRepository
import com.hr.core.repository.MoviesRepository
import com.hr.core.repository.MoviesRepositoryModel
import com.hr.core.viewmodel.ContentMovieViewStateData
import com.hr.core.viewmodel.MoviesViewState
import com.hr.models.Movie
import io.reactivex.Flowable
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val likedMoviesRepository: LikedMoviesRepository
) {
    val moviesViewStateStream: Flowable<MoviesViewState>
        get() = moviesRepository.moviesStream()
            .map<MoviesViewState> { model ->
                return@map when (model) {
                    is MoviesRepositoryModel.Data -> mergeWithLikedMovies(model)
                    MoviesRepositoryModel.NotPresent -> MoviesViewState.Loading
                    MoviesRepositoryModel.Error -> MoviesViewState.Error
                }
            }

    private fun mergeWithLikedMovies(model: MoviesRepositoryModel.Data): MoviesViewState {
        val merged = mutableListOf<ContentMovieViewStateData>()
        val likesMap: Map<String, Boolean> = likedMoviesRepository.getState()
        model.movieList.forEach { movie: Movie ->
            val name = movie.name
            likesMap[name]
                ?.let { liked: Boolean -> merged.add(ContentMovieViewStateData(movie, liked)) }
                ?: merged.add(ContentMovieViewStateData(movie, false))
        }
        return MoviesViewState.Content(merged)
    }

    fun fetch() = moviesRepository.fetch()

    fun updateLike(movieName: String, liked: Boolean) = likedMoviesRepository.updateLike(movieName, liked)
}
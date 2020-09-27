package com.hr.core.usecase

import com.hr.core.TestSchedulersProvider
import com.hr.core.repository.MoviesRepository
import com.hr.core.repository.like.LikedMoviesRepository
import com.hr.core.repository.models.MoviesRepositoryModel
import com.hr.core.viewmodel.ContentMovieViewStateData
import com.hr.core.viewmodel.MoviesViewState
import com.hr.models.Actor
import com.hr.models.Movie
import com.hr.models.MovieDetail
import io.reactivex.Flowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.Mockito.`when` as whenever

internal class GetMoviesUseCaseTest {

    private lateinit var mockMoviesRepository: MoviesRepository
    private lateinit var mockLikedMoviesRepository: LikedMoviesRepository
    private lateinit var getMoviesUseCase: GetMoviesUseCase

    @BeforeEach
    fun setup() {
        mockMoviesRepository = mock(MoviesRepository::class.java)
        mockLikedMoviesRepository = mock(LikedMoviesRepository::class.java)
        getMoviesUseCase = GetMoviesUseCase(
            mockMoviesRepository,
            mockLikedMoviesRepository,
            TestSchedulersProvider()
        )
    }

    @Test
    fun `Should map error correctly to view state error`() {
        // given
        val expectedValue = MoviesViewState.Error
        whenever(mockMoviesRepository.moviesStream()).thenReturn(
            Flowable.just(
                MoviesRepositoryModel.Error
            )
        )
        whenever(mockLikedMoviesRepository.likedMoviesStream()).thenReturn(
            Flowable.just(emptyMap())
        )

        val test = getMoviesUseCase.moviesViewStateStream.test()

        // when
        getMoviesUseCase.fetch()

        // then
        test.assertValue(expectedValue)
        test.assertNoErrors()
    }

    @Test
    fun `Should map not present data correctly to view state loading`() {
        // given
        val expectedValue = MoviesViewState.Loading
        whenever(mockMoviesRepository.moviesStream()).thenReturn(
            Flowable.just(
                MoviesRepositoryModel.NotPresent
            )
        )
        whenever(mockLikedMoviesRepository.likedMoviesStream()).thenReturn(
            Flowable.just(emptyMap())
        )

        val test = getMoviesUseCase.moviesViewStateStream.test()

        // when
        getMoviesUseCase.fetch()

        // then
        test.assertValue(expectedValue)
        test.assertNoErrors()
    }

    @Test
    fun `Should map content data correctly to view state content`() {
        // given
        val expectedValue = MoviesViewState.Content(listOf(ContentMovieViewStateData(testMovie, true)))
        whenever(mockMoviesRepository.moviesStream()).thenReturn(
            Flowable.just(
                MoviesRepositoryModel.Data(listOf(testMovie))
            )
        )
        whenever(mockLikedMoviesRepository.likedMoviesStream()).thenReturn(
            Flowable.just(mapOf(Pair(testMovie.name, true)))
        )

        val test = getMoviesUseCase.moviesViewStateStream.test()

        // when
        getMoviesUseCase.fetch()

        // then
        test.assertValue(expectedValue)
        test.assertNoErrors()
    }

    @Test
    fun `Should return not liked item if not present in like repository`() {
        // given
        whenever(mockMoviesRepository.moviesStream()).thenReturn(
            Flowable.just(
                MoviesRepositoryModel.Data(listOf(testMovie))
            )
        )
        whenever(mockLikedMoviesRepository.likedMoviesStream()).thenReturn(
            Flowable.just(emptyMap())
        )

        val test = getMoviesUseCase.moviesViewStateStream.test()

        // when
        getMoviesUseCase.fetch()

        // then
        test.assertValue { (it as MoviesViewState.Content).let { list -> !(list.movieList[0].liked) } }
        test.assertNoErrors()
    }

    @Test
    fun `Should update like`() {
        // given
        val expectedLike = true
        whenever(mockMoviesRepository.moviesStream()).thenReturn(
            Flowable.just(
                MoviesRepositoryModel.Data(listOf(testMovie))
            )
        )
        whenever(mockLikedMoviesRepository.likedMoviesStream()).thenReturn(
            Flowable.just(mapOf(Pair(testMovie.name, expectedLike)))
        )
        val test = getMoviesUseCase.moviesViewStateStream.test()

        // when
        getMoviesUseCase.updateLike(testMovie.name, expectedLike)

        // then
        test.assertValue { (it as MoviesViewState.Content).let { list -> (list.movieList[0].liked) } }
        test.assertNoErrors()
    }

    @Test
    fun `Should not emit event for like operation if there is no movie`() {
        // given
        whenever(mockMoviesRepository.moviesStream()).thenReturn(Flowable.empty())
        whenever(mockLikedMoviesRepository.likedMoviesStream()).thenReturn(
            Flowable.just(mapOf(Pair(testMovie.name, false)))
        )
        val test = getMoviesUseCase.moviesViewStateStream.test()

        // when
        getMoviesUseCase.updateLike(testMovie.name, true)

        // then
        test.assertNoValues()
    }

    @Test
    fun `Should fetch movie details with correct like status`() {
        // given
        val movieName = "Chicken Run"
        val expectedDetail = testMovieDetail.copy(movieName = movieName)
        whenever(mockMoviesRepository.movieDetailsStream(anyString())).thenReturn(
            Flowable.just(expectedDetail)
        )
        whenever(mockLikedMoviesRepository.likedMoviesStream()).thenReturn(
            Flowable.just(mapOf(Pair(movieName, true)))
        )

        // when
        val test = getMoviesUseCase.moviesDetailsFlowable(movieName).test()

        // then
        test.assertValue { it.movieDetail == expectedDetail }
        test.assertValue { it.liked }
    }

    private val testMovie: Movie = Movie("", 1)
    private val testMovieDetail: MovieDetail = MovieDetail(
        "",
        1.0f,
        listOf(Actor("John", 11, "url")),
        "descr"
    )
}
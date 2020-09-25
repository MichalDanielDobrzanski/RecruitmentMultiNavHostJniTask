package com.hr.core.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class LikedMoviesRepositoryImplTest {

    lateinit var likedMoviesRepository: LikedMoviesRepository

    @BeforeEach
    fun setup() {
        likedMoviesRepository = LikedMoviesRepositoryImpl()
    }

    @Test
    fun `Should begin with empty state for map`() {
        assertEquals(0, likedMoviesRepository.getState().size)
    }

    @Test
    fun `Should like film and cache state`() {
        // given
        val movie = "Chicken run"

        // when
        likedMoviesRepository.updateLike(movie, true)

        // then
        val state = likedMoviesRepository.getState()
        assert(state.containsKey(movie))
        assert(state[movie] ?: error(""))
        assertEquals(1, state.size)
    }

    @Test
    fun `Should unlike for film and cache state`() {
        // given
        val movie = "Fifth element"

        // when
        likedMoviesRepository.updateLike(movie, false)

        // then
        val state = likedMoviesRepository.getState()
        assert(state.containsKey(movie))
        assertFalse(state[movie]?: error(""))
        assertEquals(1, state.size)
    }

    @Test
    fun `Should make idempotent operation for like`() {
        // given
        val movie = "Fifth element"

        // when
        likedMoviesRepository.updateLike(movie, true)
        likedMoviesRepository.updateLike(movie, true)
        likedMoviesRepository.updateLike(movie, true)
        likedMoviesRepository.updateLike(movie, true)

        // then
        val state = likedMoviesRepository.getState()
        assert(state.containsKey(movie))
        assert(state[movie] ?: error(""))
        assertEquals(1, state.size)
    }

    @Test
    fun `Should make idempotent operation for unlike`() {
        // given
        val movie = "Fifth element"

        // when
        likedMoviesRepository.updateLike(movie, false)
        likedMoviesRepository.updateLike(movie, false)
        likedMoviesRepository.updateLike(movie, false)
        likedMoviesRepository.updateLike(movie, false)

        // then
        val state = likedMoviesRepository.getState()
        assert(state.containsKey(movie))
        assertFalse(state[movie] ?: error(""))
        assertEquals(1, state.size)
    }
}
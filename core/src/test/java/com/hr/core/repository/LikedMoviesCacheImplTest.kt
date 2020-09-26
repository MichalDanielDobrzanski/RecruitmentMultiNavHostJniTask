package com.hr.core.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class LikedMoviesCacheImplTest {

    lateinit var likedMoviesCache: LikedMoviesCache

    @BeforeEach
    fun setup() {
        likedMoviesCache = LikedMoviesCacheImpl()
    }

    @Test
    fun `Should begin with empty state for map`() {
        assertEquals(0, likedMoviesCache.getState().size)
    }

    @Test
    fun `Should like film and cache state`() {
        // given
        val movie = "Chicken run"

        // when
        likedMoviesCache.updateLike(movie, true)

        // then
        val state = likedMoviesCache.getState()
        assert(state.containsKey(movie))
        assert(state[movie] ?: error(""))
        assertEquals(1, state.size)
    }

    @Test
    fun `Should unlike for film and cache state`() {
        // given
        val movie = "Fifth element"

        // when
        likedMoviesCache.updateLike(movie, false)

        // then
        val state = likedMoviesCache.getState()
        assert(state.containsKey(movie))
        assertFalse(state[movie]?: error(""))
        assertEquals(1, state.size)
    }

    @Test
    fun `Should make idempotent operation for like`() {
        // given
        val movie = "Fifth element"

        // when
        likedMoviesCache.updateLike(movie, true)
        likedMoviesCache.updateLike(movie, true)
        likedMoviesCache.updateLike(movie, true)
        likedMoviesCache.updateLike(movie, true)

        // then
        val state = likedMoviesCache.getState()
        assert(state.containsKey(movie))
        assert(state[movie] ?: error(""))
        assertEquals(1, state.size)
    }

    @Test
    fun `Should make idempotent operation for unlike`() {
        // given
        val movie = "Fifth element"

        // when
        likedMoviesCache.updateLike(movie, false)
        likedMoviesCache.updateLike(movie, false)
        likedMoviesCache.updateLike(movie, false)
        likedMoviesCache.updateLike(movie, false)

        // then
        val state = likedMoviesCache.getState()
        assert(state.containsKey(movie))
        assertFalse(state[movie] ?: error(""))
        assertEquals(1, state.size)
    }
}
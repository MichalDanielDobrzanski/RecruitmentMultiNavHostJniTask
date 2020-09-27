package com.hr.test.application.di

import com.hr.core.repository.*
import com.hr.core.repository.like.LikedMoviesCache
import com.hr.core.repository.like.LikedMoviesCacheImpl
import com.hr.core.repository.like.LikedMoviesRepository
import com.hr.core.repository.like.LikedMoviesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class AppBindingsModule {

    @Binds
    abstract fun bindMoviesRepository(repository: MoviesNativeRepository): MoviesRepository

    @Binds
    abstract fun bindLikedMoviesRepository(likedMoviesRepositoryImpl: LikedMoviesCacheImpl): LikedMoviesCache

    @Binds
    abstract fun bindLikedMoviesReactiveRepository(likedMoviesReactiveRepository: LikedMoviesRepositoryImpl): LikedMoviesRepository
}
package com.hr.test.application.di

import com.hr.core.repository.*
import com.hr.core.repository.like.LikedMoviesCache
import com.hr.core.repository.like.LikedMoviesCacheImpl
import com.hr.core.repository.like.LikedMoviesRepository
import com.hr.core.repository.like.LikedMoviesRepositoryImpl
import com.hr.core.schedulers.AndroidSchedulersProvider
import com.hr.core.schedulers.SchedulersProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class AppBindingsModule {

    @Binds
    abstract fun bindMoviesRepository(moviesNativeRepository: MoviesNativeRepository): MoviesRepository

    @Binds
    abstract fun bindLikedMoviesCache(likedMoviesCacheImpl: LikedMoviesCacheImpl): LikedMoviesCache

    @Binds
    abstract fun bindLikedMoviesRepository(likedMoviesRepositoryImpl: LikedMoviesRepositoryImpl): LikedMoviesRepository

    @Binds
    abstract fun bindSchedulersProvider(androidSchedulersProvider: AndroidSchedulersProvider): SchedulersProvider
}
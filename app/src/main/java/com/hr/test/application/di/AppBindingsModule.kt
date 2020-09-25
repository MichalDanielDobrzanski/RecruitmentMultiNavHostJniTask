package com.hr.test.application.di

import com.hr.core.repository.MoviesRepository
import com.hr.core.repository.DummyMoviesRepositoryThatShouldBeReplaced
import com.hr.core.repository.LikedMoviesRepository
import com.hr.core.repository.LikedMoviesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class AppBindingsModule {

    @Binds
    abstract fun bindMoviesRepository(movieSourceImpl: DummyMoviesRepositoryThatShouldBeReplaced): MoviesRepository

    @Binds
    abstract fun bindLikedMoviesRepository(likedMoviesRepositoryImpl: LikedMoviesRepositoryImpl): LikedMoviesRepository
}
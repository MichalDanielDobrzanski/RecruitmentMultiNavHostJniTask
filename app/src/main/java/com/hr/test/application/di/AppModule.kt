package com.hr.test.application.di

import com.hr.core.repository.*
import com.hr.core.usecase.GetMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMovieSourceImpl(): DummyMoviesRepositoryThatShouldBeReplaced =
        DummyMoviesRepositoryThatShouldBeReplaced()


    @Provides
    @Singleton
    fun provideLikedMoviesRepositoryImpl(): LikedMoviesCacheImpl =
        LikedMoviesCacheImpl()

    @Provides
    @Singleton
    fun provideGerMoviesUseCase(
        moviesRepository: MoviesRepository,
        likedMoviesRepository: LikedMoviesRepository
    ): GetMoviesUseCase = GetMoviesUseCase(moviesRepository, likedMoviesRepository)
}
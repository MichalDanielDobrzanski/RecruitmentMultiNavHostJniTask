package com.hr.test.application.di

import com.hr.core.repository.DummyMoviesRepositoryThatShouldBeReplaced
import com.hr.core.repository.LikedMoviesRepository
import com.hr.core.repository.LikedMoviesRepositoryImpl
import com.hr.core.repository.MoviesRepository
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
    fun provideLikedMoviesRepositoryImpl(): LikedMoviesRepositoryImpl =
        LikedMoviesRepositoryImpl()

    @Provides
    @Singleton
    fun provideGerMoviesUseCase(
        moviesRepository: MoviesRepository,
        likedMoviesRepository: LikedMoviesRepository
    ): GetMoviesUseCase = GetMoviesUseCase(moviesRepository, likedMoviesRepository)
}
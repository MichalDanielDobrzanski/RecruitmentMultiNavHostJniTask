package com.hr.test.application.di

import com.hr.core.repository.*
import com.hr.core.repository.like.LikedMoviesCacheImpl
import com.hr.core.repository.like.LikedMoviesRepository
import com.hr.core.schedulers.AndroidSchedulersProvider
import com.hr.core.schedulers.SchedulersProvider
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
    fun provideMovieNativeRepository(): MoviesNativeRepository =
        MoviesNativeRepository()

    @Provides
    @Singleton
    fun provideLikedMoviesRepositoryImpl(): LikedMoviesCacheImpl =
        LikedMoviesCacheImpl()


    @Provides
    @Singleton
    fun provideAndroidSchedulersProvider(): AndroidSchedulersProvider =
        AndroidSchedulersProvider()

    @Provides
    @Singleton
    fun provideGerMoviesUseCase(
        moviesRepository: MoviesRepository,
        likedMoviesRepository: LikedMoviesRepository,
        schedulersProvider: SchedulersProvider
    ): GetMoviesUseCase = GetMoviesUseCase(moviesRepository, likedMoviesRepository, schedulersProvider)
}
package com.hr.core.repository;

import androidx.annotation.Keep;

import com.hr.core.repository.models.MoviesRepositoryModel;
import com.hr.models.Actor;
import com.hr.models.Movie;
import com.hr.models.MovieDetail;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.processors.BehaviorProcessor;

public class MoviesNativeRepository implements MoviesRepository {

    static {
        System.loadLibrary("movies-lib");
    }

    private final BehaviorProcessor<MoviesRepositoryModel> moviesProcessor = BehaviorProcessor.createDefault(
            (MoviesRepositoryModel) MoviesRepositoryModel.NotPresent.INSTANCE
    );

    private final ByteBuffer nativeMoviePointer;

    private final Semaphore movieDetailSemaphore = new Semaphore(0);

    private final AtomicReference<MovieDetail> movieDetailAtomicReference = new AtomicReference<>();

    private native ByteBuffer createMovieHolder();

    private native void fetchMoviesJni(ByteBuffer nativeMoviePointer);

    private native void fetchMovieDetailsJni(ByteBuffer nativeMoviePointer, String movieDetail);

    public MoviesNativeRepository() {
        nativeMoviePointer = createMovieHolder();
    }



    @Keep
    private void onMoviesFetched(final String[] marshalledMovies) {
        final List<Movie> movieList = new LinkedList<>();
        for (int i = 0; i < marshalledMovies.length - 1; i += 2) {
            final String movieName = marshalledMovies[i];
            final String movieUpdate = marshalledMovies[i + 1];
            movieList.add(new Movie(movieName, Integer.parseInt(movieUpdate)));
        }
        moviesProcessor.onNext(new MoviesRepositoryModel.Data(movieList));
    }

    @Keep
    private void onMovieDetailsFetched(
            final String movieName,
            final float score,
            final String description,
            final String[] marshalledActors
    ) {
        final List<Actor> actorList = new LinkedList<>();
        for (int i = 0; i < marshalledActors.length - 2; i += 3) {
            final String actorName = marshalledActors[i];
            final int age = Integer.parseInt(marshalledActors[i + 1]);
            final String imageUrl = marshalledActors[i + 2];
            actorList.add(new Actor(actorName, age, imageUrl));
        }
        final MovieDetail movieDetail = new MovieDetail(movieName, score, actorList, description);
        movieDetailAtomicReference.set(movieDetail);
        movieDetailSemaphore.release();
    }

    @Override
    public void fetchMovies() {
        fetchMoviesJni(nativeMoviePointer);
    }

    @NotNull
    @Override
    public Flowable<MoviesRepositoryModel> moviesStream() {
        return moviesProcessor.onBackpressureLatest();
    }

    @NotNull
    @Override
    public Flowable<MovieDetail> movieDetailsStream(@NotNull final String movieName) {
        return Flowable.create(new FlowableOnSubscribe<MovieDetail>() {
            @Override
            public void subscribe(@NotNull final FlowableEmitter<MovieDetail> emitter) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            movieDetailSemaphore.acquire();
                            emitter.onNext(movieDetailAtomicReference.get());
                        } catch (InterruptedException e) {
                            e.printStackTrace();

                        }
                        emitter.onComplete();
                    }
                }).start();
                fetchMovieDetailsJni(nativeMoviePointer, movieName);
            }
        }, BackpressureStrategy.LATEST);
    }
}
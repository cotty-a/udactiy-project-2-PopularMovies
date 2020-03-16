package com.udacity.popularmovies.database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.udacity.popularmovies.DetailsActivity;
import com.udacity.popularmovies.utilies.SampleData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.udacity.popularmovies.DetailsActivity;

public class AppRepository {
    private static  AppRepository ourInstance;



    public LiveData<List<MovieEntity>> mMovies;
    private AppDatabase mDb;

/*    All room database operations must be executed in a background thread
    One executor use for all db so that it has a queue if there are multiple db request*/
    private Executor executor = Executors.newSingleThreadExecutor();


    public static AppRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new AppRepository(context);
        }
        return ourInstance;
    }

    private AppRepository(Context context) {
        mDb = AppDatabase.getInstance(context);
        mMovies = getAllMovies();
    }


    // Get data from DB
    private LiveData<List<MovieEntity>> getAllMovies(){
        return mDb.movieDao().getAll();
    }

    public void addMovieData() {

        final MovieEntity movie = DetailsActivity.getMovieDetails();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().insertMovie(movie);


            }
        });
    }

    public MovieEntity getMovieById(int movieId) {
        return mDb.movieDao().getMovieById(movieId);

    }

    public void deleteMovie(final MovieEntity movie) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().deleteMovie(movie);
            }
        });
    }
}

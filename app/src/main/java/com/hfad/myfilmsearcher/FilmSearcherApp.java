package com.hfad.myfilmsearcher;

import android.app.Application;

import androidx.room.Room;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.hfad.myfilmsearcher.CinemaInternet.CinemaService;
import com.hfad.myfilmsearcher.FilmsInternet.FilmsService;
import com.hfad.myfilmsearcher.roomDB.FilmsDatabase;
import com.hfad.myfilmsearcher.workers.UpdateBdWorker;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FilmSearcherApp extends Application {

    public FilmsService filmsService;
    public CinemaService cinemaService;
    private FilmsDatabase mDb;
    private static FilmSearcherApp instance;

    private static final String DB_NAME = "filmsDatabase.db";

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        initRetrofit();
        initDb();
        initUpdateBdWorker();

        /*Intent intent = new Intent(this, UpdateDatabaseService.class);
        startService(intent);*/
    }

    public static FilmSearcherApp getInstance() {
        return instance;
    }

    private void initDb() {
        mDb = Room.databaseBuilder(
                this,
                FilmsDatabase.class,
                DB_NAME)
                .allowMainThreadQueries()
                .build();
    }

    private void initRetrofit() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://my-json-server.typicode.com/denis-zhuravlev/json-placeholder/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        filmsService = retrofit.create(FilmsService.class);

        Retrofit cinemaRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        cinemaService = cinemaRetrofit.create(CinemaService.class);

    }

    private void initUpdateBdWorker() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest updateBd = new PeriodicWorkRequest.Builder(UpdateBdWorker.class, 1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .addTag("WorkerBd").build();

        WorkManager.getInstance(this).enqueue(updateBd);
    }

    public FilmsDatabase getDb() {
        return mDb;
    }
}

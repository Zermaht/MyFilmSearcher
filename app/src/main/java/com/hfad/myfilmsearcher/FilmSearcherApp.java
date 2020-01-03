package com.hfad.myfilmsearcher;

import android.app.Application;



import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FilmSearcherApp extends Application {

    FilmsService filmsService;
    CinemaService cinemaService;

    private static FilmSearcherApp instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

    initRetrofit();

    }

    public static FilmSearcherApp getInstance() { return instance;}

    private void initRetrofit() {
        HttpLoggingInterceptor httpLoggingInterceptor =new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://my-json-server.typicode.com/denis-zhuravlev/json-placeholder/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        filmsService = retrofit.create(FilmsService.class);

        Retrofit cinemaRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        cinemaService = cinemaRetrofit.create(CinemaService.class);

    }
}

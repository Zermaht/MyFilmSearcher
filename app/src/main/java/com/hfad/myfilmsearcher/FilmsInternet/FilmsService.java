package com.hfad.myfilmsearcher.FilmsInternet;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;

public interface FilmsService {
    @GET("movies")
    Call<List<FilmsJson>> getMovies();

    @GET("movies")
    Observable<List<FilmsJson>> getObservableMovies();
}

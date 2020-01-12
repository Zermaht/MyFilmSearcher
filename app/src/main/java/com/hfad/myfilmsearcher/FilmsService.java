package com.hfad.myfilmsearcher;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FilmsService {
    @GET("movies")
    Call<List<FilmsJson>> getMovies();
}

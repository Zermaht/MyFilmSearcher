package com.hfad.myfilmsearcher;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CinemaService {
    @GET("json")
    Call<CinemaJson> getCinemasByLocation(@Query("location") String location, @Query("radius") int rad, @Query("type") String type, @Query("key") String key);

}

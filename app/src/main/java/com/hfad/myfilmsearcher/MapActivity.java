package com.hfad.myfilmsearcher;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map_google);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 0);
        longitude = intent.getDoubleExtra("longitude", 0);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng latLng = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.setMinZoomPreference(10);

        for (CinemaJson.Result result: MainActivity.cinemasList) {
            String cinema_name = result.getName();
            LatLng ciema_position = new LatLng(result.getGeometry().getLocation().getLat(), result.getGeometry().getLocation().getLng());

            map.addMarker(new MarkerOptions()
                    .position(ciema_position)
                    .title(cinema_name));
        }

        /*String location = latitude + "," + longitude;


        FilmSearcherApp.getInstance().cinemaService.getCinemasByLocation(location, 10000, "movie_theater", "AIzaSyA43ZBsSquDZMUZJPs_IWTeHx6A2LxZK3E").enqueue(new Callback<CinemaJson>() {
            @Override
            public void onResponse(Call<CinemaJson> call, Response<CinemaJson> response) {
                if (response.isSuccessful()){
                    CinemaJson cinemaJson = response.body();
                    List<CinemaJson.Result> results = cinemaJson.getResults();
                    double cinema_lat;
                    double cinema_lng;
                    String cinema_name;
                    for (CinemaJson.Result result : results) {
                        cinema_lat = result.getGeometry().getLocation().getLat();
                        cinema_lng = result.getGeometry().getLocation().getLng();
                        cinema_name = result.getName();
                        LatLng ciema_position = new LatLng(cinema_lat, cinema_lng);

                        map.addMarker(new MarkerOptions()
                                .position(ciema_position)
                                .title(cinema_name));
                    }

                }
            }

            @Override
            public void onFailure(Call<CinemaJson> call, Throwable t) {

            }
        });*/
    }

}

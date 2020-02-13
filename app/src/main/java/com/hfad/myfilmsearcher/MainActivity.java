package com.hfad.myfilmsearcher;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hfad.myfilmsearcher.CinemaInternet.CinemaJson;
import com.hfad.myfilmsearcher.CinemaInternet.MapActivity;
import com.hfad.myfilmsearcher.receivers.ConnectionStateMonitor;
import com.hfad.myfilmsearcher.roomDB.FilmsViewModel;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements AddFilmFragment.onAddFilmListener {

    final static String TAG = MainActivity.class.getSimpleName();
    final static int MY_REQUEST_CODE = 23;

    BottomAppBar bar;
    FloatingActionButton fab;

    static LatLng target_location;
    public static List<CinemaJson.Result> cinemasList;

    private FilmsViewModel filmsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bar);

        filmsViewModel = new ViewModelProvider(this).get(FilmsViewModel.class);

        getLastLocation();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new FilmsFragment(), FilmsFragment.TAG)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bar.getFabAlignmentMode() == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER) {
                    AddFilmFragment addFilmFragment = new AddFilmFragment();
                    addFilmFragment.setListener(MainActivity.this);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(null)
                            .replace(R.id.fragment_container, addFilmFragment, AddFilmFragment.TAG)
                            .commit();
                    bar.performShow();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
            }

        });

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_REQUEST_CODE);

        ConnectionStateMonitor connectionStateMonitor = new ConnectionStateMonitor(this);
        connectionStateMonitor.observe(this, aBoolean -> {
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_invite:
                ShareCompat.IntentBuilder
                        .from(this)
                        .setType("text/plain")
                        .setChooserTitle("Отправить приглашение...")
                        .setText("Давай посмотрим фильм?")
                        .startChooser();
                break;
            case android.R.id.home:
                BottomNavigationDrawerFragment.newInstance().show(getSupportFragmentManager(), BottomNavigationDrawerFragment.TAG);
                break;
            case R.id.action_favorites:
                if (filmsViewModel.getFavoriteFilms() != null && filmsViewModel.getFavoriteFilms().size() != 0) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .addToBackStack(null)
                            .replace(R.id.fragment_container, new FavoritesFragment(), FavoritesFragment.TAG)
                            .commit();
                    bar.performShow();
                    switchFabButton();
                    bar.replaceMenu(R.menu.menu_main_favorites);
                    break;
                } else
                    Snackbar.make(findViewById(R.id.fragment_container), "Список избранного пуст", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.action_cinema:
                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra("latitude", target_location.latitude);
                intent.putExtra("longitude", target_location.longitude);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void onAddFilmClick(String filmName, String filmDescription) {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_container, FilmsFragment.newInstance(filmName, filmDescription), FilmsFragment.TAG)
                .commit();
    }

    void switchFabButton() {
        Fragment filmsFragment = getVisibleFragment();
        if (filmsFragment instanceof FilmsFragment) {
            bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
            fab.setImageResource(R.drawable.ic_add_24px);
        } else {
            bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
            fab.setImageResource(R.drawable.ic_reply_24px);
        }
        bar.replaceMenu(R.menu.menu_main);
    }

    public Fragment getVisibleFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            locationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        target_location = new LatLng(location.getLatitude(), location.getLongitude());
                        setObservableCinemasList();
                    }
                }
            });
        }
    }

    void setCinemasList() {
        String location = target_location.latitude + "," + target_location.longitude;
        FilmSearcherApp.getInstance().cinemaService.getCinemasByLocation(location, 10000, "movie_theater", "AIzaSyA43ZBsSquDZMUZJPs_IWTeHx6A2LxZK3E").enqueue(new Callback<CinemaJson>() {
            @Override
            public void onResponse(Call<CinemaJson> call, Response<CinemaJson> response) {
                if (response.isSuccessful()) {
                    CinemaJson cinemaJson = response.body();
                    cinemasList = cinemaJson.getResults();
                }
            }

            @Override
            public void onFailure(Call<CinemaJson> call, Throwable t) {

            }
        });
    } //Больше не нужен, но оставлю для примера. Запрос делается через setObservableCinemasList()

    void setObservableCinemasList() {
        String location = target_location.latitude + "," + target_location.longitude;
        FilmSearcherApp.getInstance().cinemaService.getObservableCinemas(location, 10000, "movie_theater", "AIzaSyA43ZBsSquDZMUZJPs_IWTeHx6A2LxZK3E")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CinemaJson>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CinemaJson cinemaJson) {
                        cinemasList = cinemaJson.getResults();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        switchFabButton();
    }
}


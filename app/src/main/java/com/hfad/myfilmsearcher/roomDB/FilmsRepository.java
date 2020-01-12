package com.hfad.myfilmsearcher.roomDB;

import android.app.Application;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.hfad.myfilmsearcher.FilmSearcherApp;
import com.hfad.myfilmsearcher.FilmsJson;
import com.hfad.myfilmsearcher.MainActivity;
import com.hfad.myfilmsearcher.R;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

public class FilmsRepository {

    private FilmsDAO dao;
    private LiveData<List<FilmEntity>> allFilms;
    private FilmEntity filmById;
    private FilmEntity filmByName;
    private List<FilmEntity> films;
    private List<FilmEntity> favoriteFilms;

    FilmsRepository(Application application) {
        FilmsDatabase db = FilmsDatabase.getInstance(application);
        dao = db.filmsDAO();
        allFilms = dao.getAllFilms();
        films = dao.getFilms();
    }

    LiveData<List<FilmEntity>> getAllFilms() {
        return allFilms;
    }

    List<FilmEntity> getFilms() {
        return films;
    }

    FilmEntity getFilmById(long id) {
        filmById = dao.getFilmById(id);
        return filmById;
    }

    FilmEntity getFilmByName(String name) {
        filmByName = dao.getFilmByName(name);
        return filmByName;
    }

    List<FilmEntity> getFavoriteFilms() {
        favoriteFilms = dao.getFavoriteFilms(true);
        return favoriteFilms;
    }

    void insert(FilmEntity filmEntity) {
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(filmEntity);
            }
        });
    }

    void insertFilms(List<FilmEntity> films) {
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                dao.insertFilms(films);
            }
        });
    }

    void update(FilmEntity filmEntity) {
        dao.update(filmEntity);
    }

    void delete(FilmEntity filmEntity) {
        dao.delete(filmEntity);
    }

    void deleteAll() {
        Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
            }
        });
    }


}

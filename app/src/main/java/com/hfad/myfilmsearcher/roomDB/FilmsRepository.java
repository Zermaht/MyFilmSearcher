package com.hfad.myfilmsearcher.roomDB;

import androidx.lifecycle.LiveData;

import com.hfad.myfilmsearcher.FilmSearcherApp;

import java.util.List;
import java.util.concurrent.Executors;

public class FilmsRepository {

    private FilmsDAO dao;
    private LiveData<List<FilmEntity>> allFilms;
    private FilmEntity filmById;
    private FilmEntity filmByName;
    private List<FilmEntity> films;
    private List<FilmEntity> favoriteFilms;

   public FilmsRepository() {
        FilmsDatabase db = FilmSearcherApp.getInstance().getDb();
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

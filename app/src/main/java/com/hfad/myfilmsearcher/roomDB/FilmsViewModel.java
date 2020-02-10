package com.hfad.myfilmsearcher.roomDB;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FilmsViewModel extends AndroidViewModel {
    private FilmsRepository filmsRepository;

    private LiveData<List<FilmEntity>> allFilms;
    private List<FilmEntity> films;
    private FilmEntity filmById;
    private FilmEntity filmByName;
    private List<FilmEntity> favoriteFilms;

    public FilmsViewModel(Application application) {
        super(application);
        filmsRepository = new FilmsRepository();
        allFilms = filmsRepository.getAllFilms();
        films = filmsRepository.getFilms();
    }

    public LiveData<List<FilmEntity>> getAllFilms() {
        return allFilms;
    }

    public List<FilmEntity> getFilms() {
        return films;
    }

    public FilmEntity getFilmById(long id) {
        filmById = filmsRepository.getFilmById(id);
        return filmById;
    }

    public FilmEntity getFilmByName(String name) {
        filmByName = filmsRepository.getFilmByName(name);
        return filmByName;
    }

    public List<FilmEntity> getFavoriteFilms() {
        favoriteFilms = filmsRepository.getFavoriteFilms();
        return favoriteFilms;
    }

    public void insert(FilmEntity filmEntity) {
        if (filmsRepository.getFilmByName(filmEntity.getName()) == null) {
            filmsRepository.insert(filmEntity);
        } else {
            return;
        }
    }

    /* public void insert(FilmsJson filmsJson) {filmsRepository.insert(filmsJson);}*/

    public void insertFilms(List<FilmEntity> filmEntities) {
        filmsRepository.insertFilms(filmEntities);
    }

    public void update(FilmEntity filmEntity) {
        filmsRepository.update(filmEntity);
    }

    public void delete(FilmEntity filmEntity) {
        filmsRepository.delete(filmEntity);
    }

    public void deleteAll() {
        filmsRepository.deleteAll();
    }
}

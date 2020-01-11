package com.hfad.myfilmsearcher.roomDB;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FilmsViewModel extends AndroidViewModel {
    private FilmsRepository filmsRepository;

    private LiveData<List<FilmEntity>> allFilms;
    private FilmEntity filmById;
    private FilmEntity filmByName;
    private List<FilmEntity> favoriteFilms;

    public FilmsViewModel(Application application) {
        super(application);
        filmsRepository = new FilmsRepository(application);
        allFilms = filmsRepository.getAllFilms();
    }

    LiveData<List<FilmEntity>> getAllFilms() {return allFilms;}

    FilmEntity getFilmById(long id){
        filmById = filmsRepository.getFilmById(id);
        return filmById;
    }

    FilmEntity getFilmByName(String name){
        filmByName = filmsRepository.getFilmByName(name);
        return filmByName;
    }

    List<FilmEntity> getFavoriteFilms(){
        favoriteFilms = filmsRepository.getFavoriteFilms();
        return favoriteFilms;
    }

    void insert (FilmEntity filmEntity){
        filmsRepository.insert(filmEntity);
    }

    void insertFIlms(List<FilmEntity> filmEntities){
        filmsRepository.insertFilms(filmEntities);
    }

    void update(FilmEntity filmEntity){
        filmsRepository.update(filmEntity);
    }

    void delete(FilmEntity filmEntity){
        filmsRepository.delete(filmEntity);
    }

    void deleteAll(){filmsRepository.deleteAll();}
}

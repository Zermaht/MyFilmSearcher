package com.hfad.myfilmsearcher.roomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.hfad.myfilmsearcher.FilmsJson;

import java.util.List;

@Dao
public interface FilmsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FilmEntity filmEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFilms(List<FilmEntity> filmEntities);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(FilmEntity filmEntity);

    @Delete
    void delete(FilmEntity filmEntity);

    @Query("DELETE FROM films_table")
    void deleteAll();

    @Query("SELECT * from films_table")
    LiveData<List<FilmEntity>> getAllFilms();

    @Query("SELECT * from films_table")
    List<FilmEntity> getFilms();

    @Query("SELECT * from films_table WHERE name LIKE :search ")
    FilmEntity getFilmByName(String search);

    @Query("SELECT * from films_table WHERE _id LIKE :id")
    FilmEntity getFilmById(long id);

    @Query("SELECT * from films_table WHERE isFavorite LIKE :favorite")
    List<FilmEntity> getFavoriteFilms(boolean favorite);


}

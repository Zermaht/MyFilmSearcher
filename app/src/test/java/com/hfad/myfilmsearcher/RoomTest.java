package com.hfad.myfilmsearcher;

import android.content.Context;
import android.os.Build;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hfad.myfilmsearcher.roomDB.FilmEntity;
import com.hfad.myfilmsearcher.roomDB.FilmsDAO;
import com.hfad.myfilmsearcher.roomDB.FilmsDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.List;
import java.util.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class RoomTest {
    private FilmsDAO dao;
    private FilmsDatabase db;
    private List<FilmEntity> films;

    @Before
    public void creteDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, FilmsDatabase.class).allowMainThreadQueries().build();
        dao = db.filmsDAO();
        films = FilmEntityHelper.createListOfFilms();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void insertListOfFilms() {
        dao.insertFilms(films);
        List<FilmEntity> dbFilms = dao.getFilms();

        assertEquals(5, dbFilms.size());
    }

    @Test
    public void whenInsertFilmThenReadTheSameOne() {
        dao.insert(films.get(0));
        List<FilmEntity> dbFilms = dao.getFilms();

        assertEquals(1, dbFilms.size());
        assertTrue(FilmEntityHelper.filmsAreIdentical(films.get(0), dbFilms.get(0)));
    }

    @Test
    public void getFIlmByName() {
        FilmEntity expected = films.get(0);
        dao.insert(expected);

        FilmEntity actual = dao.getFilmByName("bob");
        assertTrue(FilmEntityHelper.filmsAreIdentical(expected, actual));
    }

    @Test
    public void getFilmById() {
        dao.insertFilms(films);

        assertEquals(films.get(0), dao.getFilmById(1));
    }

    @Test
    public void updateOneFilm() {
        dao.insert(films.get(0));
        FilmEntity updatedFilm = dao.getFilmByName("bob");
        updatedFilm.setName("Steve");
        dao.update(updatedFilm);

        assertEquals(1, dao.getFilms().size());
        assertFalse(FilmEntityHelper.filmsAreIdentical(films.get(0), dao.getFilmByName("Steve")));
    }

    @Test
    public void deleteFilm() {
        dao.insertFilms(films);
        FilmEntity film = dao.getFilmByName("bob");
        dao.delete(film);
        film = dao.getFilmByName("bob");

        assertNull(film);
        assertEquals(4, dao.getFilms().size());
    }

    @Test
    public void deleteAllFilms() {
        dao.insertFilms(films);
        dao.deleteAll();
        assertEquals(0, dao.getFilms().size());
    }

    @Test
    public void getFavoriteFilms() {
        dao.insertFilms(films);

        assertEquals(2, dao.getFavoriteFilms(true).size());
    }

}



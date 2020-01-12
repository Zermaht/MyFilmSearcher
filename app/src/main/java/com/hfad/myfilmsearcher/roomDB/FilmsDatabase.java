package com.hfad.myfilmsearcher.roomDB;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.hfad.myfilmsearcher.FilmSearcherApp;
import com.hfad.myfilmsearcher.FilmsJson;
import com.hfad.myfilmsearcher.MainActivity;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

@Database(entities = {FilmEntity.class}, version = 1, exportSchema = false)
public abstract class FilmsDatabase extends RoomDatabase {
    public abstract FilmsDAO filmsDAO();

    private static final String DB_NAME = "filmsDatabase.db";
    private static FilmsDatabase instance;

    static synchronized FilmsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static FilmsDatabase create(final Context context) {
        return Room.databaseBuilder(
                context.getApplicationContext(),
                FilmsDatabase.class,
                DB_NAME)
                .allowMainThreadQueries()
                .build();
    }

    /*private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

        }
    };*/

    /*public void downloadFilms() {
        FilmSearcherApp.getInstance().filmsService.getMovies().enqueue(new retrofit2.Callback<List<FilmsJson>>() {
            @Override
            public void onResponse(Call<List<FilmsJson>> call, Response<List<FilmsJson>> response) {
                if (response.isSuccessful()) {
                    List<FilmsJson> filmsJsons = response.body();
                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            for (FilmsJson fIlmsJson : filmsJsons) {
                                instance.filmsDAO().insert(new FilmEntity(fIlmsJson));
                                Log.d("BOBO", instance.filmsDAO().getFilmById(1).toString());
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<FilmsJson>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }*/
}

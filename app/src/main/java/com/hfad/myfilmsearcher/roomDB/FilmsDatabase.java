package com.hfad.myfilmsearcher.roomDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FilmEntity.class}, version = 1, exportSchema = false)
public abstract class FilmsDatabase extends RoomDatabase {

    public abstract FilmsDAO filmsDAO();

    private static final String DB_NAME = "filmsDatabase.db";
    private static  FilmsDatabase instance;

    static synchronized FilmsDatabase getInstance(Context context){
        if (instance == null){
            instance = create(context);
        }
        return instance;
    }

    private static FilmsDatabase create(final Context context) {
        return Room.databaseBuilder(
                context.getApplicationContext(),
                FilmsDatabase.class,
                DB_NAME)
                .build();
    }



}

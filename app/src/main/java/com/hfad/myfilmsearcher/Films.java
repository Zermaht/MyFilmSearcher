package com.hfad.myfilmsearcher;

import android.widget.TextView;

import androidx.annotation.NonNull;

public class Films {
    private String description;
    private int image;
    private String filmName;

    Films (String filmName, String description, int image) {
        this.filmName = filmName;
        this.description = description;
        this.image = image;
    }


    public String getFilmName() {
        return filmName;
    }

    public String getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }

    @NonNull
    @Override
    public String toString() {
        return filmName;
    }
}

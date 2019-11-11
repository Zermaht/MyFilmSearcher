package com.hfad.myfilmsearcher;

import android.widget.TextView;

import androidx.annotation.NonNull;

public class Films {
    private String description;
    private int image;
    private TextView filmTextView;

    Films (TextView filmTextView, String description, int image) {
        this.filmTextView = filmTextView;
        this.description = description;
        this.image = image;
    }

    public TextView getFilmTextView() {
        return filmTextView;
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
        return filmTextView.getText().toString();
    }
}

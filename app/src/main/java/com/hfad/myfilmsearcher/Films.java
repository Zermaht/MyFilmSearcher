package com.hfad.myfilmsearcher;

import android.widget.TextView;

import androidx.annotation.NonNull;

public class Films {
    private String description;
    private String filmName;
    private String imageUrl;

    Films (String filmName, String description, String image) {
        this.filmName = filmName;
        this.description = description;
        this.imageUrl = image;
    }

    Films(FIlmsJson fIlmsJson) {
        this.filmName = fIlmsJson.title;
        this.imageUrl = fIlmsJson.img;
    }


    public String getFilmName() {
        return filmName;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {return imageUrl;}

    @NonNull
    @Override
    public String toString() {
        return filmName;
    }
}

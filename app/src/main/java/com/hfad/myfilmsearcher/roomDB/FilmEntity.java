package com.hfad.myfilmsearcher.roomDB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.hfad.myfilmsearcher.FilmsJson;

@Entity(tableName = "films_table")
public class FilmEntity {

    @PrimaryKey(autoGenerate = true)
    private long _id;

    private String name;
    private String description;
    private String imgUrl;
    private boolean isFavorite;

    public FilmEntity(String name, String description, String imgUrl, boolean isFavorite) {
        this.name = name;
        this.description = description;
        this.imgUrl = imgUrl;
        this.isFavorite = isFavorite;
    }

    public FilmEntity(FilmsJson filmsJson) {
        this.name = filmsJson.title;
        this.imgUrl = filmsJson.img;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }

    @Override
    public String toString() {
        return "FilmEntity{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", isFavorite=" + isFavorite +
                '}';
    }
}

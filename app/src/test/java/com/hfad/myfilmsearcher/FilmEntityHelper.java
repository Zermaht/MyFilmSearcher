package com.hfad.myfilmsearcher;

import com.hfad.myfilmsearcher.roomDB.FilmEntity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FilmEntityHelper {

    public static List<FilmEntity> createListOfFilms(){
        List<FilmEntity> list = new ArrayList<>();
        FilmEntity bob = new FilmEntity("bob", "bob", "bob", false);
        bob.set_id(1);
        FilmEntity mike = new FilmEntity("mike", "mike", "mike", true);
        mike.set_id(2);
        FilmEntity jon = new FilmEntity("jon", "jon", "jon", false);
        jon.set_id(3);
        FilmEntity sam = new FilmEntity("sam", "sam", "sam", true);
        sam.set_id(4);
        FilmEntity niki = new FilmEntity("niki", "niki", "niki", false);
        niki.set_id(5);

        list.add(bob);
        list.add(mike);
        list.add(jon);
        list.add(sam);
        list.add(niki);
        return list;
    }

    @NotNull
    public static Boolean filmsAreIdentical(FilmEntity filmOne, FilmEntity filmTwo){
        String nameOne = filmOne.getName();
        String nameTwo = filmTwo.getName();
        String imageOne = filmOne.getImgUrl();
        String imageTwo = filmTwo.getImgUrl();
        String descriptionOne = filmOne.getDescription();
        String descriptionTwo = filmTwo.getDescription();
        long idOne = filmOne.get_id();
        long idTwo = filmTwo.get_id();

        if (nameOne.equals(nameTwo) && imageOne.equals(imageTwo) && descriptionOne.equals(descriptionTwo) && idOne == idTwo){
            return true;
        } else return false;
    }

}

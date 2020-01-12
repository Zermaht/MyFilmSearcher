package com.hfad.myfilmsearcher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hfad.myfilmsearcher.roomDB.FilmEntity;
import com.hfad.myfilmsearcher.roomDB.FilmsViewModel;

import java.util.List;

public class FavoritesFragment extends Fragment {
    static final String TAG = FavoritesFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private FilmsViewModel filmsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_favorites);
        filmsViewModel = new ViewModelProvider(this).get(FilmsViewModel.class);
        initRecycler();
    }

    private void initRecycler() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        List<FilmEntity> favoriteFilms = filmsViewModel.getFavoriteFilms();

        String[] filmNames = new String[favoriteFilms.size()];
        for (int i = 0; i < filmNames.length; i++) {
            filmNames[i] = favoriteFilms.get(i).getName();
        }

        String[] filmImageUrl = new String[favoriteFilms.size()];
        for (int i = 0; i < filmNames.length; i++) {
            filmImageUrl[i] = favoriteFilms.get(i).getImgUrl();
        }

        FavoritesFilmsAdapter favoritesFilmsAdapter = new FavoritesFilmsAdapter(filmNames, filmImageUrl);
        favoritesFilmsAdapter.setFavoritesFilms(favoriteFilms);
        recyclerView.setAdapter(favoritesFilmsAdapter);

        favoritesFilmsAdapter.setFavoritesListener(new FavoritesFilmsAdapter.FavoritesListener() {
            @Override
            public void onLongClick(int position, CardView cardView) {
                TextView textView = cardView.findViewById(R.id.favorites_name);
                String filmName = textView.getText().toString();
                FilmEntity filmEntity = filmsViewModel.getFilmByName(filmName);
                PopupMenu popupMenu = new PopupMenu(getContext(), cardView);
                popupMenu.inflate(R.menu.favorites_popmenu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        filmEntity.setFavorite(false);
                        filmsViewModel.update(filmEntity);
                        initRecycler();
                        return true;
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).switchFabButton();
    }
}

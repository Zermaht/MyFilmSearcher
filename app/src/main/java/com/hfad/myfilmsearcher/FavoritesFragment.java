package com.hfad.myfilmsearcher;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavoritesFragment extends Fragment {
    static final String TAG = FavoritesFragment.class.getSimpleName();
    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_favorites);

        initRecycler();

    }

    private void initRecycler() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        String[] filmNames = new String[MainActivity.favorites.size()];
        for (int i = 0; i < filmNames.length; i++) {
            filmNames[i] = MainActivity.favorites.get(i).getFilmName();
        }

        String[] filmImageUrl = new String[MainActivity.favorites.size()];
        for (int i = 0; i < filmNames.length; i++) {
            filmImageUrl[i] = MainActivity.favorites.get(i).getImageUrl();
        }


        FavoritesFilmsAdapter favoritesFilmsAdapter = new FavoritesFilmsAdapter(filmNames, filmImageUrl);
        recyclerView.setAdapter(favoritesFilmsAdapter);

        favoritesFilmsAdapter.setFavoritesListener(new FavoritesFilmsAdapter.FavoritesListener() {
            @Override
            public void onLongClick(int position, CardView cardView) {
                PopupMenu popupMenu = new PopupMenu(getContext(), cardView);
                popupMenu.inflate(R.menu.favorites_popmenu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        MainActivity.favorites.remove(position);
                        recyclerView.getAdapter().notifyItemRemoved(position);
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

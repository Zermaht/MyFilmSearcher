package com.hfad.myfilmsearcher;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.snackbar.Snackbar;
import com.hfad.myfilmsearcher.roomDB.FilmEntity;
import com.hfad.myfilmsearcher.roomDB.FilmsViewModel;

import java.util.ArrayList;
import java.util.List;

public class FilmsFragment extends Fragment {
    static final String TAG = FilmsFragment.class.getSimpleName();
    private static final String EXTRA_NEW_FILM_NAME = "new film name";
    private static final String EXTRA_NEW_FILM_DESCRIPTION = "new film description";

    private FilmsViewModel filmsViewModel;
    private CaptionedImageAdapter adapter;

    static FilmsFragment newInstance(String filmName, String filmDescription) {
        FilmsFragment filmsFragment = new FilmsFragment();

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_NEW_FILM_NAME, filmName);
        bundle.putString(EXTRA_NEW_FILM_DESCRIPTION, filmDescription);
        filmsFragment.setArguments(bundle);
        return filmsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).switchFabButton();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_films, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_main);
        filmsViewModel = new ViewModelProvider(this).get(FilmsViewModel.class);

        if (getArguments() != null) {
            String filmName = getArguments().getString(EXTRA_NEW_FILM_NAME);
            String filmDescription = getArguments().getString(EXTRA_NEW_FILM_DESCRIPTION);
            String filmImgUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSH2h5v6TcvdTnyM9_RUd1C6WOt_ht1ALW55aPa9J26k0ZBj_v7&s";
            filmsViewModel.insert(new FilmEntity(filmName, filmDescription, filmImgUrl, false));
            setArguments(null);
        }
        adapter = new CaptionedImageAdapter(getContext());
        setListener(adapter);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setFilms(filmsViewModel.getFilms());
        filmsViewModel.getAllFilms().observe(this, new Observer<List<FilmEntity>>() {
            @Override
            public void onChanged(List<FilmEntity> filmEntities) {
                adapter.setFilms(filmEntities);
            }
        });
    }

    private void setListener(CaptionedImageAdapter adapter) {
        adapter.setListener(new CaptionedImageAdapter.Listener() {
            @Override
            public void onClick(int position, CardView cardView) {
                TextView textView = cardView.findViewById(R.id.info_film);
                String filmName = textView.getText().toString();
                FilmEntity film = filmsViewModel.getFilmByName(filmName);

                String filmDescription = film.getDescription();
                String filmImage = film.getImgUrl();

                ObjectAnimator scaleY = ObjectAnimator.ofFloat(cardView, "scaleY", 1.01f);
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(cardView, "scaleX", 1.01f);

                final AnimatorSet set = new AnimatorSet();
                set.play(scaleY).with(scaleX);
                set.setDuration(300);
                set.setInterpolator(new DecelerateInterpolator());

                set.start();
                set.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        cardView.setScaleY(1);
                        cardView.setScaleX(1);
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, DetailFragment.newInstance(filmImage, filmDescription))
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null)
                                .commit();
                        BottomAppBar bar = getActivity().findViewById(R.id.bottom_app_bar);
                        bar.performHide();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
            }

            @Override
            public void onLongClick(int position, CardView cardView) {
                TextView textView = cardView.findViewById(R.id.info_film);
                String filmName = textView.getText().toString();
                FilmEntity film = filmsViewModel.getFilmByName(filmName);
                //TODO Тут приходит ноль в film. Разобраться почему
                PopupMenu popupMenu = new PopupMenu(getContext(), cardView);
                popupMenu.inflate(R.menu.films_fragment_popmenu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (!film.isFavorite()) {
                            film.setFavorite(true);
                            filmsViewModel.update(film);
                            Snackbar.make(getActivity().findViewById(R.id.fragment_container), "Фильм добавлен в избранное", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(getActivity().findViewById(R.id.fragment_container), "Этот фильм уже есть в избранном", Snackbar.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}


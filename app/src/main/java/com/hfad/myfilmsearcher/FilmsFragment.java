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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmsFragment extends Fragment {

    static final String TAG = FilmsFragment.class.getSimpleName();
    private static final String EXTRA_NEW_FILM_NAME = "new film name";
    private static final String EXTRA_NEW_FILM_DESCRIPTION = "new film description";

    static ArrayList<Films> filmsArray = new ArrayList<>();
    private RecyclerView recyclerView;

    public static FilmsFragment newInstance(String filmName, String filmDescription) {
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

        recyclerView = view.findViewById(R.id.recycler_view_main);

        if (getArguments() != null) {
            String filmName = getArguments().getString(EXTRA_NEW_FILM_NAME);
            String filmDescription = getArguments().getString(EXTRA_NEW_FILM_DESCRIPTION);
            filmsArray.add(new Films(filmName, filmDescription, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSH2h5v6TcvdTnyM9_RUd1C6WOt_ht1ALW55aPa9J26k0ZBj_v7&s"));
            setArguments(null);
        }

        downloadFilms();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        String[] filmNames = new String[filmsArray.size()];
        for (int i = 0; i < filmNames.length; i++) {
            filmNames[i] = filmsArray.get(i).getFilmName();
        }


        String[] filmsImagesUrl = new String[filmsArray.size()];
        for (int i = 0; i < filmsImagesUrl.length; i++) {
            filmsImagesUrl[i] = filmsArray.get(i).getImageUrl();
        }

        CaptionedImageAdapter adapter = new CaptionedImageAdapter(filmNames, filmsImagesUrl);
        recyclerView.setAdapter(adapter);

        adapter.setListener(new CaptionedImageAdapter.Listener() {
            @Override
            public void onClick(int position, CardView cardView) {
                String filmDescription = filmsArray.get(position).getDescription();
                String filmImage = filmsImagesUrl[position];

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
                String filmDescription = filmsArray.get(position).getDescription();
                String filmImage = filmsImagesUrl[position];
                String filmName = filmsArray.get(position).getFilmName();

                PopupMenu popupMenu = new PopupMenu(getContext(), cardView);
                popupMenu.inflate(R.menu.films_fragment_popmenu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        boolean isFavorite = false;
                        for (Films x : MainActivity.favorites) {
                            if (x.getFilmName().equals(filmName)) {
                                Snackbar.make(getActivity().findViewById(R.id.fragment_container), "Этот фильм уже есть в избранном", Snackbar.LENGTH_SHORT).show();
                                isFavorite = true;
                            }
                        }
                        if (!isFavorite) {
                            MainActivity.favorites.add(new Films(filmName, filmDescription, filmImage));
                            Snackbar.make(getActivity().findViewById(R.id.fragment_container), "Фильм добавлен в избранное", Snackbar.LENGTH_SHORT).show();
                        }

                        return true;
                    }
                });

            }
        });

    }

    public void downloadFilms() {
        if (filmsArray.isEmpty()) {
            FilmSearcherApp.getInstance().filmsService.getMovies().enqueue(new Callback<List<FIlmsJson>>() {
                @Override
                public void onResponse(Call<List<FIlmsJson>> call, Response<List<FIlmsJson>> response) {
                    if (response.isSuccessful()) {
                        List<FIlmsJson> fIlmsJsons = response.body();
                        for (FIlmsJson fIlmsJson : fIlmsJsons) {
                            filmsArray.add(new Films(fIlmsJson));
                        }
                        initRecyclerView();
                    } else {
                        Toast.makeText(getContext(), "FAIL " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<FIlmsJson>> call, Throwable t) {
                    Toast.makeText(getContext(), "FAILURE " + t.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        } else {
            initRecyclerView();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    static int returnCount() {
        return filmsArray.size();
    }
}


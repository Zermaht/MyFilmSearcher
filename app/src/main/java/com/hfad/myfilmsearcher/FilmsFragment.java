package com.hfad.myfilmsearcher;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FilmsFragment extends Fragment {

    static final String TAG = FilmsFragment.class.getSimpleName();
    private static final String EXTRA_NEW_FILM_NAME = "new film name";
    private static final String EXTRA_NEW_FILM_DESCRIPTION = "new film description";

    private static ArrayList<Films> filmsArray = new ArrayList<>();
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
        if (filmsArray.size() == 0) {
            filmsArray.add(new Films(getString(R.string.shoushenk), getString(R.string.shoushenk_opisanie), R.drawable.pobeg_iz_shoushenka));
            filmsArray.add(new Films(getString(R.string.zelenaia_milia), getString(R.string.zelenaia_milia_opisanie), R.drawable.zelenaia_milia));
            filmsArray.add(new Films(getString(R.string.forest_gamp), getString(R.string.forest_gamp_opisanie), R.drawable.forest_gamp));
        }
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
            filmsArray.add(new Films(filmName, filmDescription, R.drawable.android_logo));
            setArguments(null);
        }

        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        String[] filmNames = new String[filmsArray.size()];
        for (int i = 0; i<filmNames.length; i++){
            filmNames[i] = filmsArray.get(i).getFilmName();
        }

        int[] filmImages = new int[filmsArray.size()];
        for (int i = 0; i<filmNames.length; i++){
            filmImages[i] = filmsArray.get(i).getImage();
        }

        CaptionedImageAdapter adapter = new CaptionedImageAdapter(filmNames, filmImages);
        recyclerView.setAdapter(adapter);

        adapter.setListener(new CaptionedImageAdapter.Listener() {
            @Override
            public void onClick(int position, CardView cardView) {
                String filmDescription = filmsArray.get(position).getDescription();
                int filmImage = filmImages[position];

                ObjectAnimator scaleY = ObjectAnimator.ofFloat(cardView, "scaleY", 1.01f);
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(cardView, "scaleX", 1.01f);

                final AnimatorSet set =new AnimatorSet();
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
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    static int returnCount() {
        return filmsArray.size();
    }
}


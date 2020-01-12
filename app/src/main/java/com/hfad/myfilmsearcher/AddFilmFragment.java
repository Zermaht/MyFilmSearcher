package com.hfad.myfilmsearcher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.hfad.myfilmsearcher.roomDB.FilmsViewModel;

public class AddFilmFragment extends Fragment {
    static final String TAG = AddFilmFragment.class.getSimpleName();
    private FilmsViewModel filmsViewModel;

    private onAddFilmListener listener = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_film, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText filmName = view.findViewById(R.id.new_film_name);
        TextInputEditText filmDescription = view.findViewById(R.id.new_film_description);

        filmsViewModel = new ViewModelProvider(this).get(FilmsViewModel.class);

        view.findViewById(R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filmNameSend = filmName.getText().toString();
                String filmDescriptionSend = filmDescription.getText().toString();

                if (listener != null && !filmDescriptionSend.equals("") && !filmNameSend.equals("") && filmsViewModel.getFilmByName(filmNameSend) == null) {
                    listener.onAddFilmClick(filmNameSend, filmDescriptionSend);
                    MainActivity activity = (MainActivity) getActivity();
                    activity.switchFabButton();
                } else if (filmsViewModel.getFilmByName(filmNameSend) != null) {
                    Snackbar.make(getActivity().findViewById(R.id.fragment_container), "Фильм с таким названием уже был добавлен", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(getActivity().findViewById(R.id.fragment_container), "Не заполнены необходимые поля", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void setListener(onAddFilmListener listener) {
        this.listener = listener;
    }

    public interface onAddFilmListener {
        void onAddFilmClick(String filmName, String filmDescription);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).switchFabButton();
    }
}

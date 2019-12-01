package com.hfad.myfilmsearcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;

public class AddNewFilmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_film);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final LinearLayout bottomSheet = findViewById(R.id.design_bottom_sheet);

        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                ImageView imageButton = findViewById(R.id.image_bbottom_sheet);
                if (i == STATE_EXPANDED) {
                    imageButton.setImageResource(R.drawable.ic_keyboard_arrow_down_24px);
                } if (i == STATE_COLLAPSED) {
                    imageButton.setImageResource(R.drawable.ic_keyboard_arrow_up_24px);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

    }

    public void addNewFilm(View view) {
        TextInputEditText filmName = findViewById(R.id.new_film_name);
        TextInputEditText filmDescription = findViewById(R.id.new_film_description);

        String filmNameSend = filmName.getText().toString();
        String filmDescriptionSend = filmDescription.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("filmName", filmNameSend);
        intent.putExtra("filmDescription", filmDescriptionSend);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

}

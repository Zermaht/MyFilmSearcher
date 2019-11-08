package com.hfad.myfilmsearcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView dPhoto = findViewById(R.id.img_detail_photo);
        TextView dTextView = findViewById(R.id.textView2);

        Intent intent = getIntent();
        int dDescriptionID = intent.getIntExtra("imgDescription", 0);

        dPhoto.setImageResource(intent.getIntExtra("img", 0));
        dPhoto.setContentDescription(getResources().getString(dDescriptionID));

        if (dDescriptionID != 0) {
            switch (dDescriptionID) {
                case R.string.shoushenk:
                    dTextView.setText(getResources().getString(R.string.shoushenk_opisanie));
                    break;
                case R.string.forest_gamp:
                    dTextView.setText(getResources().getString(R.string.forest_gamp_opisanie));
                    break;
                case R.string.zelenaia_milia:
                    dTextView.setText(getResources().getString(R.string.zelenaia_milia_opisanie));
                    break;
            }
        }
    }
}

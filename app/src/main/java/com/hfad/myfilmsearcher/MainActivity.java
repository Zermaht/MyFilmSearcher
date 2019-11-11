package com.hfad.myfilmsearcher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final static String TAG = MainActivity.class.getSimpleName();

    private TextView mShoushenkTextView;
    private TextView mGreenMile;
    private TextView mGamp;
    private String answerCheckBox;
    private String answerComment;

    private ArrayList<Films> filmsArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mShoushenkTextView = findViewById(R.id.textView_shoushenk);
        mGreenMile = findViewById(R.id.textView_zelenaia_milia);
        mGamp = findViewById(R.id.textView_forest_gump);

        filmsArray.add(new Films(findViewById(R.id.textView_shoushenk), getString(R.string.shoushenk_opisanie), R.drawable.pobeg_iz_shoushenka));
        filmsArray.add(new Films(findViewById(R.id.textView_zelenaia_milia), getString(R.string.zelenaia_milia_opisanie), R.drawable.zelenaia_milia));
        filmsArray.add(new Films(findViewById(R.id.textView_forest_gump), getString(R.string.forest_gamp_opisanie), R.drawable.forest_gamp));

        if (savedInstanceState != null) {
            mShoushenkTextView.setTextColor(savedInstanceState.getInt("Shoushenk_color"));
            mGreenMile.setTextColor(savedInstanceState.getInt("Green_mile_color"));
            mGamp.setTextColor(savedInstanceState.getInt("Gamp_color"));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Shoushenk_color", mShoushenkTextView.getCurrentTextColor());
        outState.putInt("Green_mile_color", mGreenMile.getCurrentTextColor());
        outState.putInt("Gamp_color", mGamp.getCurrentTextColor());
    }

    final static int OUR_REQUEST_CODE = 100;
    final static String ANSWER_CHECKBOX = "answer_checkbox";
    final static String ANSWER_COMMENT = "answer_comment";

    public void onClickButton(View view) {
        Button button = findViewById(view.getId());
        int id = Integer.parseInt(button.getContentDescription().toString());
        Films film = filmsArray.get(id);

        TextView filmTextView = film.getFilmTextView();
        filmTextView.setTextColor(getResources().getColor(R.color.colorAccent));

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("img", film.getImage());
        intent.putExtra("Description", film.getDescription());

        startActivityForResult(intent, OUR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OUR_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                answerCheckBox = data.getStringExtra(ANSWER_CHECKBOX);
                answerComment = data.getStringExtra(ANSWER_COMMENT);
            }
            Log.d(TAG, "Check box: " + answerCheckBox + "\n" + "Comment: " + answerComment);
        }
    }

    public void onClickInviteButton(View view) {
        String mimeType = "text/plain";
        Button button = findViewById(view.getId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Давай посмотрим ").append(button.getContentDescription().toString());

        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle("Отправить приглашение...")
                .setText(stringBuilder.toString())
                .startChooser();
    }
}


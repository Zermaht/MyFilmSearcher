package com.hfad.myfilmsearcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mShoushenkTextView;
    private TextView mGreenMile;
    private TextView mGamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         mShoushenkTextView = findViewById(R.id.textView_shoushenk);
         mGreenMile = findViewById(R.id.textView_zelenaia_milia);
         mGamp = findViewById(R.id.textView_forest_gump);

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

    public void onClickButton(View view) {
        int id = view.getId();
        TextView mTextView = null;
        int mImage = 0;
        int mDescription = 0;

        switch (id) {
            case R.id.button_shoushenk:
                mTextView = mShoushenkTextView;
                mImage = R.drawable.pobeg_iz_shoushenka;
                mDescription = R.string.shoushenk;
                break;
            case R.id.button_forest_gamp:
                mTextView = mGamp;
                mImage = R.drawable.forest_gamp;
                mDescription = R.string.forest_gamp;
                break;
            case R.id.button_zelenaia_milia:
                mTextView = mGreenMile;
                mImage = R.drawable.zelenaia_milia;
                mDescription = R.string.zelenaia_milia;
                break;
        }

        if (mTextView != null && mImage != 0 && mDescription != 0) {
            mTextView.setTextColor(getResources().getColor(R.color.colorAccent));

            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("img", mImage);
            intent.putExtra("imgDescription", mDescription);

            startActivity(intent);

        }
    }

    public void onClickInviteButton(View view) {
        String mimeType = "text/plain";
        int id = view.getId();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Давай посмотрим ");

        switch (id) {
            case R.id.button_shoushenk_INVITE:
                stringBuilder.append("Побег из Шоушенка?");
                break;
            case R.id.button_forest_gamp_INVITE:
                stringBuilder.append("Форест Гамп?");
                break;
            case R.id.button_zelenaia_milia_INVITE:
                stringBuilder.append("Зеленую милю?");
                break;
        }

        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle("Отправить приглашение...")
                .setText(stringBuilder.toString())
                .startChooser();
    }
}

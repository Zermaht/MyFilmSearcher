package com.hfad.myfilmsearcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClickButton(View view) {
        int id = view.getId();
        TextView mTextView = null;
        int mImage = 0;
        String mDescription = null;

        switch (id) {
            case R.id.button_shoushenk:
                mTextView = findViewById(R.id.textView_shoushenk);
                mImage = R.drawable.pobeg_iz_shoushenka;
                mDescription = getResources().getString(R.string.shoushenk);
                break;
            case R.id.button_forest_gamp:
                mTextView = findViewById(R.id.textView_forest_gump);
                mImage = R.drawable.forest_gamp;
                mDescription = getResources().getString(R.string.forest_gamp);
                break;
            case R.id.button_zelenaia_milia:
                mTextView = findViewById(R.id.textView_zelenaia_milia);
                mImage = R.drawable.zelenaia_milia;
                mDescription = getResources().getString(R.string.zelenaia_milia);
                break;
        }

        if (mTextView != null && mImage != 0 && mDescription != null) {
            mTextView.setTextColor(getResources().getColor(R.color.colorAccent));

            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("img", mImage);
            intent.putExtra("imgDescription", mDescription);

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }
    }
}

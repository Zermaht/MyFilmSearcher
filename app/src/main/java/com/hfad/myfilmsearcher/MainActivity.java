package com.hfad.myfilmsearcher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClickShoushenk(View view) {
         TextView shoushenk = findViewById(R.id.textView_shoushenk);
         shoushenk.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    public void onClickGreenMile(View view) {
        TextView greenMile = findViewById(R.id.textView_zelenaia_milia);
        greenMile.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    public void onClickGamp(View view) {
        TextView mGamp = findViewById(R.id.textView_forest_gump);
        mGamp.setTextColor(getResources().getColor(R.color.colorAccent));
    }
}

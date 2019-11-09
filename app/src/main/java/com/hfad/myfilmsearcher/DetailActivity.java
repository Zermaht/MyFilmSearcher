package com.hfad.myfilmsearcher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    final static String TAG = DetailActivity.class.getSimpleName();


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

    @Override
    public void onBackPressed() {
        CheckBox checkBox = findViewById(R.id.checkBox);
        String like_checkbox;

        if (checkBox.isChecked()) {
            like_checkbox = "Нравится";
        } else like_checkbox = "Не нравится";

        EditText comment = findViewById(R.id.comment_EditText);
        String commentResult = comment.getText().toString();

        Intent intentR = new Intent();
        intentR.putExtra(MainActivity.ANSWER_CHECKBOX,like_checkbox);
        intentR.putExtra(MainActivity.ANSWER_COMMENT, commentResult);
        setResult(RESULT_OK, intentR);

        Log.d(TAG, "Check box sended: " + like_checkbox + "\n" + "Comment sended: " + commentResult);

        super.onBackPressed();
    }

}

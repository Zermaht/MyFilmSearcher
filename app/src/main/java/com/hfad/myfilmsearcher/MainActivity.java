package com.hfad.myfilmsearcher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final static String TAG = MainActivity.class.getSimpleName();

    private String answerCheckBox;
    private String answerComment;

    static ArrayList<Films> filmsArray = new ArrayList<>();

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filmsArray.add(new Films(getString(R.string.shoushenk), getString(R.string.shoushenk_opisanie), R.drawable.pobeg_iz_shoushenka));
        filmsArray.add(new Films(getString(R.string.zelenaia_milia), getString(R.string.zelenaia_milia_opisanie), R.drawable.zelenaia_milia));
        filmsArray.add(new Films(getString(R.string.forest_gamp), getString(R.string.forest_gamp_opisanie), R.drawable.forest_gamp));

        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_invite) {
            ShareCompat.IntentBuilder
                    .from(this)
                    .setType("text/plain")
                    .setChooserTitle("Отправить приглашение...")
                    .setText("Давай посмотрим фильм?")
                    .startChooser();
        }
        return true;
    }

    //Инициализация recyclerView
    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view_main);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
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
            public void onClick(int position) {
                String filmDescription = filmsArray.get(position).getDescription();
                int filmImage = filmImages[position];

                Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                intent.putExtra("img", filmImage);
                intent.putExtra("Description", filmDescription);
                startActivityForResult(intent, OUR_REQUEST_CODE);
            }
        });

    }

    final static int OUR_REQUEST_CODE = 100;
    final static String ANSWER_CHECKBOX = "answer_checkbox";
    final static String ANSWER_COMMENT = "answer_comment";


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


package com.hfad.myfilmsearcher;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ShareCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    final static String TAG = MainActivity.class.getSimpleName();

    private String answerCheckBox;
    private String answerComment;

    static ArrayList<Films> filmsArray = new ArrayList<>();

    private RecyclerView recyclerView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        filmsArray.add(new Films(getString(R.string.shoushenk), getString(R.string.shoushenk_opisanie), R.drawable.pobeg_iz_shoushenka));
        filmsArray.add(new Films(getString(R.string.zelenaia_milia), getString(R.string.zelenaia_milia_opisanie), R.drawable.zelenaia_milia));
        filmsArray.add(new Films(getString(R.string.forest_gamp), getString(R.string.forest_gamp_opisanie), R.drawable.forest_gamp));

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
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
            public void onClick(int position, CardView cardView) {
                String filmDescription = filmsArray.get(position).getDescription();
                int filmImage = filmImages[position];

                ObjectAnimator rotation = ObjectAnimator.ofFloat(cardView, "rotation", 5);

                final AnimatorSet set =new AnimatorSet();
                set.play(rotation);
                set.setDuration(500);
                set.setInterpolator(new DecelerateInterpolator());

                set.start();
                set.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        cardView.setRotation(0);
                        Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                        intent.putExtra("img", filmImage);
                        intent.putExtra("Description", filmDescription);
                        startActivityForResult(intent, OUR_REQUEST_CODE);
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

}


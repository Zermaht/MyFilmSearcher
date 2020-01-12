package com.hfad.myfilmsearcher.roomDB;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.hfad.myfilmsearcher.FilmSearcherApp;
import com.hfad.myfilmsearcher.FilmsJson;
import com.hfad.myfilmsearcher.MainActivity;
import com.hfad.myfilmsearcher.R;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

public class UpdateDatabaseService extends IntentService {
    final static String CHANEL_ID = "234";
    FilmsDatabase database;

    public UpdateDatabaseService() {
        super("UpdateDatabaseService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        database = FilmsDatabase.getInstance(getApplicationContext());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        downloadFilms();
    }

    public void downloadFilms() {
        FilmSearcherApp.getInstance().filmsService.getMovies().enqueue(new retrofit2.Callback<List<FilmsJson>>() {
            @Override
            public void onResponse(Call<List<FilmsJson>> call, Response<List<FilmsJson>> response) {
                if (response.isSuccessful()) {
                    List<FilmsJson> filmsJsons = response.body();
                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            for (FilmsJson fIlmsJson : filmsJsons) {
                                database.filmsDAO().insert(new FilmEntity(fIlmsJson));
                            }
                            showMessage();
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<List<FilmsJson>> call, Throwable t) {
                t.printStackTrace();
                showMessage();
            }
        });
    }

    private void showMessage(){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "System";
            String description = "System notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager =getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANEL_ID)
                .setSmallIcon(R.drawable.ic_feedback_black_24dp)
                .setContentTitle("System")
                .setContentText("Database was updated")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(2, builder.build());
    }
}

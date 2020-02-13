package com.hfad.myfilmsearcher.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.hfad.myfilmsearcher.FilmSearcherApp;
import com.hfad.myfilmsearcher.FilmsInternet.FilmsJson;
import com.hfad.myfilmsearcher.MainActivity;
import com.hfad.myfilmsearcher.R;
import com.hfad.myfilmsearcher.roomDB.FilmEntity;
import com.hfad.myfilmsearcher.roomDB.FilmsDatabase;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;

public class UpdateBdWorker extends Worker {
   public final static String CHANEL_ID = "234";
    FilmsDatabase db;

    public UpdateBdWorker(@NonNull Context mContext, @NonNull WorkerParameters workerParams) {
        super(mContext, workerParams);
        db = FilmSearcherApp.getInstance().getDb();

    }

    @NonNull
    @Override
    public Result doWork() {
        Context applicationContext = getApplicationContext();
        downloadFilms();
        showMessage(applicationContext);
        return Result.success();
    }

    private void downloadFilms() {
        FilmSearcherApp.getInstance().filmsService.getMovies().enqueue(new retrofit2.Callback<List<FilmsJson>>() {
            @Override
            public void onResponse(Call<List<FilmsJson>> call, Response<List<FilmsJson>> response) {
                if (response.isSuccessful()) {
                    List<FilmsJson> filmsJsons = response.body();
                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            for (FilmsJson filmsJson : filmsJsons) {
                                if (db.filmsDAO().getFilmByName(filmsJson.title) == null) {
                                    db.filmsDAO().insert(new FilmEntity(filmsJson));
                                }
                            }
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<List<FilmsJson>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void showMessage(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "System";
            String description = "System notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANEL_ID)
                .setSmallIcon(R.drawable.ic_feedback_black_24dp)
                .setContentTitle("System")
                .setContentText("Database was updated")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(2, builder.build());
    }
}

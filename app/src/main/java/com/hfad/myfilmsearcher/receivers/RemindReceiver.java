package com.hfad.myfilmsearcher.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.hfad.myfilmsearcher.R;
import com.hfad.myfilmsearcher.RemindDialog;
import com.hfad.myfilmsearcher.workers.UpdateBdWorker;

public class RemindReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String filmName = intent.getStringExtra(RemindDialog.FILM_NAME);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, UpdateBdWorker.CHANEL_ID)
                .setSmallIcon(R.drawable.ic_feedback_black_24dp)
                .setContentTitle("Напоминание")
                .setContentText(filmName)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(2, builder.build());

    }
}

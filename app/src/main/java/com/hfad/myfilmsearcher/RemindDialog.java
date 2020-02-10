package com.hfad.myfilmsearcher;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.hfad.myfilmsearcher.receivers.RemindReceiver;

import java.util.Calendar;

public class RemindDialog extends Dialog {
    public final static String FILM_NAME = "name";
    private Calendar calendar = Calendar.getInstance();
    Context mContext;
    TextView date;

    public RemindDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_remind);

        EditText filmName = findViewById(R.id.film_name_dialog);

        date = findViewById(R.id.date_dialog);
        date.setOnClickListener(v -> {
            setDate(v);
            setTime(v);
        });

        Button button = findViewById(R.id.remind_button);
        button.setOnClickListener(v -> {
            if (TextUtils.isEmpty(filmName.getText())) {
                Toast.makeText(mContext, "Название не мсжет быть пустым", Toast.LENGTH_SHORT).show();
            } else {
                AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(mContext, RemindReceiver.class);
                intent.putExtra(FILM_NAME, filmName.getText().toString());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                dismiss();
            }

        });
    }

    private void setInitialDateAndTime() {
        date.setText(DateUtils.formatDateTime(
                mContext,
                calendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_TIME));
    }

    private void setDate(View v) {
        new DatePickerDialog(mContext,
                d,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void setTime(View v) {
        new TimePickerDialog(mContext,
                t,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true)
                .show();
    }

    DatePickerDialog.OnDateSetListener d = (view, year, month, dayOfMonth) -> {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setInitialDateAndTime();
    };

    TimePickerDialog.OnTimeSetListener t = (view, hourOfDay, minute) -> {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        setInitialDateAndTime();
    };
}

package com.example.gdk19_nukipratama;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gdk19_nukipratama.Notification.DailyReminder;
import com.example.gdk19_nukipratama.Notification.NotificationTime;
import com.example.gdk19_nukipratama.Notification.ReleaseReminder;

import java.util.Calendar;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_screen);

        checkPrefs();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 1000L);
    }

    private void checkPrefs() {
        SharedPreferences mSettings = getApplicationContext().getSharedPreferences("GDK19_Nuki", Context.MODE_PRIVATE);
        startAlarm(getApplicationContext(), NotificationTime.daily_hour, NotificationTime.daily_minute, DailyReminder.class, 1);
        startAlarm(getApplicationContext(), NotificationTime.release_hour, NotificationTime.release_minute, ReleaseReminder.class, 2);
        if (!mSettings.getBoolean("release_switch", false)) {
            cancelAlarm(getApplicationContext(), ReleaseReminder.class, 2);
            Toast.makeText(getApplicationContext(), "Release Alarm Inactive", Toast.LENGTH_SHORT).show();
        }
        if (!mSettings.getBoolean("daily_switch", false)) {
            cancelAlarm(getApplicationContext(), DailyReminder.class, 1);
            Toast.makeText(getApplicationContext(), "Daily Alarm Inactive", Toast.LENGTH_SHORT).show();
        }
    }

    private void startAlarm(Context context, int hour, int minute, Class cls, int reqCode) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        Intent intent = new Intent(context, cls);
        PendingIntent pintent = PendingIntent.getService(context, reqCode, intent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);
    }

    private void cancelAlarm(Context context, Class cls, int reqCode) {
        Intent intent = new Intent(getApplicationContext(), cls);
        PendingIntent pintent = PendingIntent.getService(context, reqCode, intent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pintent);
    }
}
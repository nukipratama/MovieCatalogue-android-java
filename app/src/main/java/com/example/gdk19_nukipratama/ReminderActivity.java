package com.example.gdk19_nukipratama;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gdk19_nukipratama.Notification.DailyReminder;
import com.example.gdk19_nukipratama.Notification.NotificationTime;
import com.example.gdk19_nukipratama.Notification.ReleaseReminder;

import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity {

    Switch releaseSwitch;
    Switch dailySwitch;
    SharedPreferences.Editor editor;
    SharedPreferences mSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        actionBar();
        releaseSwitch = findViewById(R.id.release_switch);
        dailySwitch = findViewById(R.id.daily_switch);

        mSettings = getApplicationContext().getSharedPreferences("GDK19_Nuki", Context.MODE_PRIVATE);
        editor = mSettings.edit();

        checkPrefs();

        releaseSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    startAlarm(getApplicationContext(), NotificationTime.release_hour, NotificationTime.release_minute, ReleaseReminder.class, 2);
                    editor.putBoolean("release_switch", b);
                    editor.apply();
                } else {
                    cancelAlarm(getApplicationContext(), ReleaseReminder.class, 2);
                    editor.putBoolean("release_switch", b);
                    editor.apply();
                }
            }
        });

        dailySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    startAlarm(getApplicationContext(), NotificationTime.daily_hour, NotificationTime.daily_minute, DailyReminder.class, 1);
                    editor.putBoolean("daily_switch", b);
                    editor.apply();
                } else {
                    cancelAlarm(getApplicationContext(), DailyReminder.class, 1);
                    editor.putBoolean("daily_switch", b);
                    editor.apply();
                }
            }
        });

    }

    private void actionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.reminder_action_title));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void checkPrefs() {
        startAlarm(getApplicationContext(), NotificationTime.daily_hour, NotificationTime.daily_minute, DailyReminder.class, 1);
        startAlarm(getApplicationContext(), NotificationTime.release_hour, NotificationTime.release_minute, ReleaseReminder.class, 2);
        releaseSwitch.setChecked(true);
        dailySwitch.setChecked(true);
        if (!mSettings.getBoolean("release_switch", false)) {
            cancelAlarm(getApplicationContext(), ReleaseReminder.class, 2);
            releaseSwitch.setChecked(false);
        }
        if (!mSettings.getBoolean("daily_switch", false)) {
            cancelAlarm(getApplicationContext(), DailyReminder.class, 1);
            dailySwitch.setChecked(false);
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

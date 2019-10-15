package com.example.gdk19_nukipratama.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.example.gdk19_nukipratama.MainActivity;
import com.example.gdk19_nukipratama.R;

public class DailyReminder extends Service {
    public static final int NOTIFICATION_ID = 1;
    public static String CHANNEL_ID = "daily_channel";
    public static CharSequence CHANNEL_NAME = "GDK Movie Catalogue Channel";

    public DailyReminder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startNotification();
        return START_STICKY;
    }

    private void startNotification() {

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0,
                notificationIntent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_videocam_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_videocam_black_24dp))
                .setContentTitle(getResources().getString(R.string.notif_daily_title))
                .setContentText(getResources().getString(R.string.notif_daily_text))
                .setSubText(getResources().getString(R.string.notif_daily_subtext))
                .setAutoCancel(true)
                .setContentIntent(intent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mBuilder.setChannelId(CHANNEL_ID);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }
        Notification notification = mBuilder.build();
        if (mNotificationManager != null) {
            mNotificationManager.notify(NOTIFICATION_ID, notification);
        }
    }
}

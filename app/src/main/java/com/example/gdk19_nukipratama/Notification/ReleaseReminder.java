package com.example.gdk19_nukipratama.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.gdk19_nukipratama.ApiClient;
import com.example.gdk19_nukipratama.MainActivity;
import com.example.gdk19_nukipratama.Movie.Movie;
import com.example.gdk19_nukipratama.Movie.MovieInterface;
import com.example.gdk19_nukipratama.Movie.MovieResponse;
import com.example.gdk19_nukipratama.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ReleaseReminder extends Service {
    private final static String API_KEY = "fd779f23434a798e13c0d6e760ee3075";
    private final static String GROUP_KEY_EMAILS = "group_key_movies";
    private final static int NOTIFICATION_REQUEST_CODE = 200;
    private static final int MAX_NOTIFICATION = 2;
    public static CharSequence CHANNEL_NAME = "GDK Movie Catalogue Channel";
    private final List<NotificationItem> stackNotif = new ArrayList<>();
    private ArrayList<Movie> movies;
    private int notifCount = 0;

    public ReleaseReminder() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        getDiscover();
        return START_STICKY;
    }


    private void getDiscover() {
        final Locale current = getResources().getConfiguration().locale;
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = df.format(c);
        MovieInterface mapiService = ApiClient.getClient().create(MovieInterface.class);
        Call<MovieResponse> mcall = mapiService.getRelease(API_KEY, current.toLanguageTag(), date, date);
        mcall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                movies = (ArrayList<Movie>) response.body().getResults();
                for (int i = 0; i < movies.size(); i++) {

                    stackNotif.add(new NotificationItem(movies.get(i).getId(), movies.get(i).getName(), movies.get(i).getDesc()));

                    notifCount++;
                }
                sendNotif();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void sendNotif() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_videocam_black_24dp);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder;

        String CHANNEL_ID = "release_channel";
        if (notifCount < MAX_NOTIFICATION) {
            mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(stackNotif.get(notifCount).getSender())
                    .setContentText(stackNotif.get(notifCount).getMessage())
                    .setSmallIcon(R.drawable.ic_videocam_black_24dp)
                    .setLargeIcon(largeIcon)
                    .setGroup(GROUP_KEY_EMAILS)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        } else {
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                    .setBigContentTitle(notifCount + " new movies")
                    .setSummaryText("Movie Release Reminder");

            for (int i = 0; i < movies.size(); i++) {
                inboxStyle.addLine(stackNotif.get(i).getSender() + ": " + stackNotif.get(i).getMessage());
            }
            mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(notifCount + " new movies")
                    .setContentText("Movie Release Reminder")
                    .setSmallIcon(R.drawable.ic_videocam_black_24dp)
                    .setGroup(GROUP_KEY_EMAILS)
                    .setGroupSummary(true)
                    .setContentIntent(pendingIntent)
                    .setStyle(inboxStyle)
                    .setAutoCancel(true);

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            mBuilder.setChannelId(CHANNEL_ID);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }
        Notification notification = mBuilder.build();
        if (mNotificationManager != null) {
            mNotificationManager.notify(notifCount, notification);
        }
    }

}

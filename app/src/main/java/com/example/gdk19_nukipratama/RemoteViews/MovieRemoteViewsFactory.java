package com.example.gdk19_nukipratama.RemoteViews;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;
import com.example.gdk19_nukipratama.DB.Movie.MovieDB;
import com.example.gdk19_nukipratama.DB.Movie.MovieData;
import com.example.gdk19_nukipratama.Favorites.Movie.FavMovieWidget;
import com.example.gdk19_nukipratama.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

class MovieRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final Context mContext;
    ArrayList<MovieData> data = new ArrayList<>();
    int mAppWidgetId;
    private AppWidgetTarget appWidgetTarget;

    MovieRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

    }

    @Override
    public void onCreate() {

    }


    @Override
    public void onDataSetChanged() {
        MovieDB db = Room.databaseBuilder(mContext, MovieDB.class, "tb_movie").allowMainThreadQueries().build();
        data.addAll(Arrays.asList(db.movieDAO().selectAllMovies()));
    }

    @Override
    public void onDestroy() {
        //required
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public RemoteViews getViewAt(final int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.movie_widget_item);

        if (position <= getCount()) {
            MovieData item = data.get(position);

            appWidgetTarget = new AppWidgetTarget(mContext, R.id.imageView, rv, mAppWidgetId);

            String imagePath = "https://image.tmdb.org/t/p/original" + item.getGambar();

            Bitmap bmp = null;
            try {
                bmp = Glide.with(mContext)
                        .asBitmap()
                        .load(imagePath)
                        .into(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL).get();

            } catch (InterruptedException | ExecutionException e) {
                Log.d("Widget Load Error", "error");

            }

            rv.setImageViewBitmap(R.id.imageView, bmp);

            Bundle extras = new Bundle();
            extras.putInt(FavMovieWidget.EXTRA_ITEM, position);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);

            rv.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        }
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


}
package com.example.gdk19_consumer.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.gdk19_consumer.model.MovieDao;

public class DatabaseContract {
    public static final String TABLE_MOVIE = "tb_movie";
    public static final String CONTENT_AUTHORITY = "com.example.gdk19_nukipratama";
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_MOVIE)
            .build();

    public static MovieDao getMovieDao(Cursor cursor) {
        int id = (int) cursor.getLong(cursor.getColumnIndex(MovieColumns.idBrg));
        String title = cursor.getString(cursor.getColumnIndex(MovieColumns.title));
        String date = cursor.getString(cursor.getColumnIndex(MovieColumns.date));
        String desc = cursor.getString(cursor.getColumnIndex(MovieColumns.desc));
        String image = cursor.getString(cursor.getColumnIndex(MovieColumns.image));
        String vote = cursor.getString(cursor.getColumnIndex(MovieColumns.vote));
        MovieDao movieDao = new MovieDao(id, title, date, desc, image, vote);

        return movieDao;
    }


    public static final class MovieColumns implements BaseColumns {
        public static final String idBrg = "_id";
        public static final String title = "judul";
        public static final String date = "tanggal";
        public static final String desc = "deskripsi";
        public static final String image = "gambar";
        public static final String vote = "rating";
    }

}

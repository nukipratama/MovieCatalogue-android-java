package com.example.gdk19_nukipratama.Provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

import com.example.gdk19_nukipratama.DB.Movie.MovieDB;
import com.example.gdk19_nukipratama.DB.Movie.MovieData;

import java.util.ArrayList;
import java.util.Arrays;

public class MovieProvider extends ContentProvider {

    private static final int MOVIES = 100;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(DbContract.CONTENT_AUTHORITY,
                DbContract.TABLE_MOVIE,
                MOVIES);
    }

    private MovieDB db;

    @Override
    public boolean onCreate() {

        return true;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        db = Room.databaseBuilder(getContext(), MovieDB.class, "tb_movie").allowMainThreadQueries().build();

        MatrixCursor myCursor = new MatrixCursor(new String[]{
                DbContract.MovieColumns.barangId,
                DbContract.MovieColumns.title,
                DbContract.MovieColumns.date,
                DbContract.MovieColumns.desc,
                DbContract.MovieColumns.image,
                DbContract.MovieColumns.vote
        });

        try {
            ArrayList<MovieData> daftar = new ArrayList<>();
            daftar.addAll(Arrays.asList(db.movieDAO().selectAllMovies()));
            for (MovieData movieData : daftar) {
                Object[] rowData = new Object[]{
                        movieData.getBarangId(),
                        movieData.getJudul(),
                        movieData.getTanggal(),
                        movieData.getDeskripsi(),
                        movieData.getGambar(),
                        movieData.getRating()
                };
                myCursor.addRow(rowData);
                Log.v("provider", movieData.toString());
            }

        } finally {
            db.close();

        }
        myCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return myCursor;

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, final ContentValues contentValues) {
        db = Room.databaseBuilder(getContext(), MovieDB.class, "tb_movie").allowMainThreadQueries().build();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        try {
            MovieData movieData = new MovieData();
            movieData.setBarangId((int) contentValues.get(DbContract.MovieColumns.barangId));
            movieData.setJudul(contentValues.get(DbContract.MovieColumns.title).toString());
            movieData.setTanggal(contentValues.get(DbContract.MovieColumns.date).toString());
            movieData.setDeskripsi(contentValues.get(DbContract.MovieColumns.desc).toString());
            movieData.setGambar(contentValues.get(DbContract.MovieColumns.image).toString());
            movieData.setRating(contentValues.get(DbContract.MovieColumns.vote).toString());
            db.movieDAO().insertMovie(movieData);
            returnUri = ContentUris.withAppendedId(DbContract.CONTENT_URI, '1');

            getContext().getContentResolver().notifyChange(uri, null);
        } finally {
            db.close();
        }
        return returnUri;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int nrUpdated = 0;
        if (nrUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return nrUpdated;

    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        return count;
    }

}
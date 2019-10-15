package com.example.gdk19_nukipratama.DB.Movie;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MovieData.class}, version = 1, exportSchema = false)
public abstract class MovieDB extends RoomDatabase {

    public abstract MovieDAO movieDAO();


}

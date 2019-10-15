package com.example.gdk19_nukipratama.DB.Show;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ShowData.class}, version = 1, exportSchema = false)
public abstract class ShowDB extends RoomDatabase {

    public abstract ShowDAO showDAO();

}

package com.example.gdk19_nukipratama.DB.Movie;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


@Dao
public interface MovieDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMovie(MovieData movie);

    @Query("SELECT * FROM `tb_movie`")
    MovieData[] selectAllMovies();

    @Delete
    int deleteMovie(MovieData movieData);

    @Query("SELECT count(*) FROM tb_movie WHERE `_id` = :id")
    int checkFav(int id);

    @Query("DELETE FROM `tb_movie` WHERE `_id` = :id")
    int deleteID(int id);


}

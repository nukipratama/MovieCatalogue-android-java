package com.example.gdk19_nukipratama.DB.Show;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ShowDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertShow(ShowData show);

    @Query("SELECT * FROM `tb_show`")
    ShowData[] selectAllShows();

    @Query("SELECT count(*) FROM tb_show WHERE barangId = :id")
    int checkFav(int id);

    @Delete
    int deleteShow(ShowData showData);

    @Query("DELETE FROM `tb_show` WHERE `barangId` = :id")
    int deleteID(int id);
}

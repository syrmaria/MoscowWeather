package com.syrovama.moscowweather;

import java.util.List;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface WeatherDAO {
    @Query("SELECT * FROM Weather")
    List<Weather> getAll();

    @Query("SELECT * FROM Weather WHERE date = :date")
    Weather get(String date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Weather> weatherList);

    @Query("DELETE FROM Weather")
    void deleteAll();
}

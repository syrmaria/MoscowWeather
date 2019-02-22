package com.syrovama.moscowweather;

import java.util.List;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import io.reactivex.Flowable;
import io.reactivex.Observable;

@Dao
public interface WeatherDAO {
    @Query("SELECT * FROM Weather")
    Flowable<List<Weather>> getAll();

    @Query("SELECT * FROM Weather WHERE id = :id")
    Weather get(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Weather> weatherList);

    @Query("DELETE FROM Weather")
    void deleteAll();
}

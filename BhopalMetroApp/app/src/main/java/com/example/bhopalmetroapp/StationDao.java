package com.example.bhopalmetroapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Station> stations);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Station station);

    @Query("SELECT * FROM stations ORDER BY name COLLATE NOCASE")
    List<Station> getAll();

    @Query("SELECT * FROM stations WHERE station_code = :code LIMIT 1")
    Station findByCode(int code);

    @Query("SELECT COUNT(*) FROM stations")
    int count();
}

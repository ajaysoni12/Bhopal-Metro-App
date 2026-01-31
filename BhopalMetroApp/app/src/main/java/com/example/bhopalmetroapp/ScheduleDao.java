package com.example.bhopalmetroapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScheduleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Schedule> schedules);

    @Query("SELECT * FROM schedules WHERE station_code = :code")
    List<Schedule> findByStation(int code);
}

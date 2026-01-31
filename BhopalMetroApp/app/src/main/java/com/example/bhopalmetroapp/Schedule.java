package com.example.bhopalmetroapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "schedules")
public class Schedule {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "station_code")
    public int stationCode;

    @ColumnInfo(name = "weekday")
    public int weekday; // 0=Sun..6=Sat

    @ColumnInfo(name = "departure_times")
    public String departureTimesCsv; // simple CSV of HH:mm values for demo

    public Schedule() {
    }
}

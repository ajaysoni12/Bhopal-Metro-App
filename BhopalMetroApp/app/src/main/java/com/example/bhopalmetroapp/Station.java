package com.example.bhopalmetroapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "stations")
public class Station {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "station_code")
    public int stationCode;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "line")
    public String line;

    @ColumnInfo(name = "lat")
    public Double lat;

    @ColumnInfo(name = "lon")
    public Double lon;

    public Station() {
    }

    @Override
    public String toString() {
        return name + " (" + stationCode + ")";
    }
}

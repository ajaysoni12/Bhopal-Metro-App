package com.example.bhopalmetroapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
public class FavoriteStation {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "station_name")
    public String stationName;

    @ColumnInfo(name = "station_code")
    public int stationCode;

    @ColumnInfo(name = "added_at")
    public long addedAt;

    public FavoriteStation() {
    }

    @Override
    public String toString() {
        return stationName + " (" + stationCode + ")";
    }
}

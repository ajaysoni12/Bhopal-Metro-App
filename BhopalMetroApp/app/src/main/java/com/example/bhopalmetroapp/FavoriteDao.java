package com.example.bhopalmetroapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteStation favorite);

    @Query("DELETE FROM favorites WHERE station_code = :code")
    void deleteByCode(int code);

    @Query("SELECT * FROM favorites ORDER BY added_at DESC")
    List<FavoriteStation> getAll();

    @Query("SELECT COUNT(*) FROM favorites WHERE station_code = :code")
    int exists(int code);
}

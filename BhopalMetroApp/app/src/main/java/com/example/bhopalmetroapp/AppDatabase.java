package com.example.bhopalmetroapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {FavoriteStation.class, Station.class, Schedule.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "bhopal_local.db";
    private static volatile AppDatabase INSTANCE;

    public abstract FavoriteDao favoriteDao();
    public abstract StationDao stationDao();
    public abstract ScheduleDao scheduleDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {      synchronized (AppDatabase.class) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build();

                }
            }
        }
        return INSTANCE;
    }
}

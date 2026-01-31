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
                        .addCallback(new RoomDatabase.Callback() {
                            @Override
                            public void onCreate(@androidx.annotation.NonNull androidx.sqlite.db.SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                // Seed DB on first create from packaged JSON (stations + schedules) and add sample favorites
                                java.util.concurrent.Executors.newSingleThreadExecutor().execute(() -> {
                                    try {
                                        LocalStationDataSource src = new LocalStationDataSource(context.getApplicationContext());
                                        java.util.List<Station> stations = src.loadAndCacheStations();
                                        // load schedules for first few stations
                                        for (int i = 0; i < Math.min(5, stations.size()); i++) {
                                            src.loadSchedulesForStation(stations.get(i).stationCode);
                                        }

                                        // add 2 sample favorites so UI isn't empty in demos
                                        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
                                        if (database.favoriteDao().exists(stations.get(0).stationCode) == 0) {
                                            FavoriteStation f1 = new FavoriteStation();
                                            f1.stationCode = stations.get(0).stationCode;
                                            f1.stationName = stations.get(0).name;
                                            f1.addedAt = System.currentTimeMillis();

                                            FavoriteStation f2 = new FavoriteStation();
                                            f2.stationCode = stations.get(Math.min(2, stations.size()-1)).stationCode;
                                            f2.stationName = stations.get(Math.min(2, stations.size()-1)).name;
                                            f2.addedAt = System.currentTimeMillis() - 1000L;

                                            database.favoriteDao().insert(f1);
                                            database.favoriteDao().insert(f2);
                                        }
                                    } catch (Exception ignored) {
                                    }
                                });
                            }
                        })
                        .build();

                }
            }
        }
        return INSTANCE;
    }
}

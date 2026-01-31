package com.example.bhopalmetroapp;

import android.content.Context;

import java.util.List;

public class StationRepository {
    private static StationRepository INSTANCE;
    private final LocalStationDataSource local;
    private final android.content.Context appContext;

    private StationRepository(android.content.Context context) {
        this.appContext = context.getApplicationContext();
        local = new LocalStationDataSource(context);
    }

    public static synchronized StationRepository getInstance(android.content.Context context) {
        if (INSTANCE == null) INSTANCE = new StationRepository(context.getApplicationContext());
        return INSTANCE;
    }

    public java.util.List<Station> loadStations() {
        // try DB first
        AppDatabase db = AppDatabase.getInstance(appContext);
        try {
            if (db.stationDao().count() > 0) {
                return db.stationDao().getAll();
            }
        } catch (Exception ignored) {
        }
        // fallback to packaged JSON and cache
        return local.loadAndCacheStations();
    }

    public java.util.List<Schedule> loadSchedulesForStation(int stationCode) {
        AppDatabase db = AppDatabase.getInstance(appContext);
        try {
            java.util.List<Schedule> s = db.scheduleDao().findByStation(stationCode);
            if (s != null && !s.isEmpty()) return s;
        } catch (Exception ignored) {
        }
        return local.loadSchedulesForStation(stationCode);
    }
}

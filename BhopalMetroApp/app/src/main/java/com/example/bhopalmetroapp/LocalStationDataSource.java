package com.example.bhopalmetroapp;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class LocalStationDataSource {
    private final Context context;
    private final MetroUtilities metroUtilities;

    public LocalStationDataSource(Context context) {
        this.context = context.getApplicationContext();
        this.metroUtilities = new MetroUtilities(context);
    }

    /**
     * Parse packaged JSON and return station models (does NOT require network).
     * Also persists to Room so subsequent loads are fast.
     */
    public List<Station> loadAndCacheStations() {
        List<Station> out = new ArrayList<>();
        try {
            // reuse existing parser to get station list and mapping
            java.util.HashMap<String, Integer> stationToCode = new java.util.HashMap<>();
            java.util.HashMap<Integer, String> codeToStation = new java.util.HashMap<>();
            java.util.HashMap<String, String> stationToLines = new java.util.HashMap<>();

            ArrayList<String> names = metroUtilities.getStationList(stationToCode, codeToStation, stationToLines);
            for (String name : names) {
                Station s = new Station();
                s.name = name;
                s.stationCode = stationToCode.getOrDefault(name, -1);
                s.line = stationToLines.getOrDefault(name, "");
                out.add(s);
            }

            // persist to Room (best-effort)
            AppDatabase db = AppDatabase.getInstance(context);
            db.stationDao().insertAll(out);

        } catch (Exception e) {
            Log.w("LocalDataSrc", "failed to load stations", e);
        }
        return out;
    }

    public List<Schedule> loadSchedulesForStation(int stationCode) {
        List<Schedule> schedules = new ArrayList<>();
        try {
            ArrayList<ArrayList<String>> arr = metroUtilities.getArrivalDepartureTime(stationCode);
            if (arr.size() >= 2) {
                Schedule s = new Schedule();
                s.stationCode = stationCode;
                // store departures CSV for weekday=0 as demo
                StringBuilder sb = new StringBuilder();
                for (String t : arr.get(1)) {
                    if (sb.length() > 0) sb.append(',');
                    sb.append(t);
                }
                s.departureTimesCsv = sb.toString();
                s.weekday = 0;
                schedules.add(s);
                AppDatabase.getInstance(context).scheduleDao().insertAll(schedules);
            }
        } catch (Exception e) {
            Log.w("LocalDataSrc", "failed to load schedule", e);
        }
        return schedules;
    }
}

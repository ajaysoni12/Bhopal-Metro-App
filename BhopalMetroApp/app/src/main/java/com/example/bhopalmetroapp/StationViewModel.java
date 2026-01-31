package com.example.bhopalmetroapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StationViewModel extends AndroidViewModel {
    private final StationRepository repo;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<List<Station>> stations = new MutableLiveData<>(new ArrayList<>());

    public StationViewModel(@NonNull Application application) {
        super(application);
        repo = StationRepository.getInstance(application);
    }

    public LiveData<List<Station>> getStations() {
        return stations;
    }

    public void loadStations() {
        executor.execute(() -> {
            List<Station> s = repo.loadStations();
            stations.postValue(s);
        });
    }

    public void loadSchedules(int stationCode, androidx.lifecycle.MutableLiveData<List<Schedule>> out) {
        executor.execute(() -> out.postValue(repo.loadSchedulesForStation(stationCode)));
    }
}

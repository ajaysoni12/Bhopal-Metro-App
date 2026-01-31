package com.example.bhopalmetroapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrainTimeViewModel extends AndroidViewModel {
    private final StationRepository repo;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final MutableLiveData<List<Schedule>> schedules = new MutableLiveData<>();

    public TrainTimeViewModel(@NonNull Application application) {
        super(application);
        repo = StationRepository.getInstance(application);
    }

    public LiveData<List<Schedule>> getSchedules() {
        return schedules;
    }

    public void loadSchedules(int stationCode) {
        executor.execute(() -> schedules.postValue(repo.loadSchedulesForStation(stationCode)));
    }
}

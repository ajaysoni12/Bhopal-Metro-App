package com.example.bhopalmetroapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class TrainTime extends AppCompatActivity {

    AutoCompleteTextView edtStation;
    Button btnFindTrainTime;
    TabLayout tabLayout;
    ImageButton btnBack;
    ViewPager viewPager;

    MetroUtilities metroUtilities = new MetroUtilities(TrainTime.this);
    HashMap<String, String> stationToLines = new HashMap<>();
    HashMap<String, Integer> stationToCode = new HashMap<>();
    HashMap<Integer, String> codeToStation = new HashMap<>();
    ArrayList<String> allStationList = new ArrayList<>();
    ViewPagerTrainTimeAdapter viewPagerTrainTimeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_time);

        init();

        allStationList = metroUtilities.getStationList(stationToCode, codeToStation, stationToLines);

        viewPagerTrainTimeAdapter = new ViewPagerTrainTimeAdapter(getSupportFragmentManager(), new ArrayList<>());
        viewPager.setAdapter(viewPagerTrainTimeAdapter);
        tabLayout.setupWithViewPager(viewPager);


        ArrayAdapter<String> stationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, allStationList);
        edtStation.setAdapter(stationAdapter);
        edtStation.setThreshold(1); // when user enter one character list will be show there
        edtStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtStation.showDropDown();
            }
        });

        // Use ViewModel to load schedules (falls back to cached DB / packaged JSON)
        TrainTimeViewModel vm = new androidx.lifecycle.ViewModelProvider(this).get(TrainTimeViewModel.class);

        btnFindTrainTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String station = edtStation.getText().toString();
                if (station.isEmpty() || !stationToCode.containsKey(station)) {
                    edtStation.setError("Please enter valid station!");
                    Toast.makeText(TrainTime.this, "Please enter valid station!", Toast.LENGTH_SHORT).show();
                    return;
                }

                final int code = stationToCode.get(station);

                final ProgressDialog progressDialog = new ProgressDialog(TrainTime.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Set the style to spinner
                progressDialog.setCancelable(false); // Make the dialog not cancelable
                progressDialog.show();

                vm.getSchedules().observe(TrainTime.this, schedules -> {
                    if (schedules == null || schedules.isEmpty()) {
                        progressDialog.dismiss();
                        viewPager.setVisibility(View.GONE);
                        tabLayout.setVisibility(View.GONE);
                        Toast.makeText(TrainTime.this, "No schedule available", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // convert to expected adapter format (arrival/departure lists)
                    ArrayList<ArrayList<String>> arrivalDepartureTime = new ArrayList<>();
                    ArrayList<String> arrival = new ArrayList<>();
                    ArrayList<String> departure = new ArrayList<>();
                    for (String t : schedules.get(0).departureTimesCsv.split(",")) departure.add(t);
                    arrivalDepartureTime.add(arrival);
                    arrivalDepartureTime.add(departure);

                    progressDialog.dismiss();
                    viewPager.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                    viewPagerTrainTimeAdapter.updateData(arrivalDepartureTime);
                });

                // trigger load (ViewModel will use repository -> DB -> packaged JSON)
                vm.loadSchedules(code);
            }
        });

        // Support deep-linking / quick open from other screens: prefill station and run search
        String prefill = getIntent().getStringExtra("station_name");
        if (prefill != null && stationToCode.containsKey(prefill)) {
            edtStation.setText(prefill);
            // small delay so UI is ready
            edtStation.postDelayed(() -> btnFindTrainTime.performClick(), 200);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {
        edtStation = findViewById(R.id.edtStation);
        btnFindTrainTime = findViewById(R.id.btnFindTrainTime);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        btnBack = findViewById(R.id.btnBack);
    }


}
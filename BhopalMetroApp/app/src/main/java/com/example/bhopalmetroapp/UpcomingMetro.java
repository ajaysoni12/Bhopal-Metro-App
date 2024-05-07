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

public class UpcomingMetro extends AppCompatActivity {

    AutoCompleteTextView edtStation;
    Button btnFindUpMetro;

    ImageButton btnBack;
    TabLayout tabLayout;
    ViewPager viewPager;

    MetroUtilities metroUtilities = new MetroUtilities(UpcomingMetro.this);

    ArrayList<JunctionDetails> allJunctionList = new ArrayList<>();
    HashMap<String, String> stationToLines = new HashMap<>();
    HashMap<String, Integer> stationToCode = new HashMap<>();
    HashMap<Integer, String> codeToStation = new HashMap<>();

    ArrayList<String> allStationList = new ArrayList<>();
    ViewPagerUpcomingMetroAdapter viewPagerUpcomingMetroAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_metro);


        init();

        allJunctionList = metroUtilities.getJunctionDetailsList();
        allStationList = metroUtilities.getStationList(stationToCode, codeToStation, stationToLines);

        viewPagerUpcomingMetroAdapter = new ViewPagerUpcomingMetroAdapter(getSupportFragmentManager(), new ArrayList<>());
        viewPager.setAdapter(viewPagerUpcomingMetroAdapter);
        tabLayout.setupWithViewPager(viewPager);

        ArrayList<String> junctionName = new ArrayList<>();
        for (int i = 0; i < allJunctionList.size(); i++) {
            junctionName.add(allJunctionList.get(i).getName());
        }

        ArrayAdapter<String> stationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, junctionName);
        edtStation.setAdapter(stationAdapter);
        edtStation.setThreshold(1); // when user enter one character list will be show there

        edtStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtStation.showDropDown();
            }
        });

        btnFindUpMetro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String station = edtStation.getText().toString();
                if (station.isEmpty() || !stationToCode.containsKey(station)) {
                    edtStation.setError("Please enter valid station!");
                    Toast.makeText(UpcomingMetro.this, "Please enter valid station!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ProgressDialog progressDialog = new ProgressDialog(UpcomingMetro.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Set the style to spinner
                progressDialog.setCancelable(false); // Make the dialog not cancelable
                progressDialog.show();

                ArrayList<ArrayList<String>> allJunctions = new ArrayList<>();
                ArrayList<String> upcomingMetro = new ArrayList<>();

                for (int i = 0; i < allJunctionList.size(); i++) {
                    if (allJunctionList.get(i).getName().equals(station)) {
                        upcomingMetro.add("" + allJunctionList.get(i).getUpcomingMetro());
                        break;
                    }
                }

                allJunctions.add(upcomingMetro);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        viewPager.setVisibility(View.VISIBLE);
                        tabLayout.setVisibility(View.VISIBLE);
                        viewPagerUpcomingMetroAdapter.updateData(allJunctions);
                    }
                }, 1000);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void init() {
        edtStation = findViewById(R.id.edtStation);
        btnFindUpMetro = findViewById(R.id.btnFindUpcomingMetro);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        btnBack = findViewById(R.id.btnBack);

    }

}
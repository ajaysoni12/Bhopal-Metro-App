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

public class FacilitiesParking extends AppCompatActivity {

    AutoCompleteTextView edtStation;
    Button btnFaciParking;
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageButton btnBack;
    MetroUtilities metroUtilities = new MetroUtilities(FacilitiesParking.this);

    ArrayList<JunctionDetails> allJunctionList = new ArrayList<>();
    HashMap<String, String> stationToLines = new HashMap<>();
    HashMap<String, Integer> stationToCode = new HashMap<>();
    HashMap<Integer, String> codeToStation = new HashMap<>();

    ArrayList<String> allStationList = new ArrayList<>();
    ViewPagerFacilitiesParkingAdapter viewPagerFacilitiesParkingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilities_parking);

        init();

        allJunctionList = metroUtilities.getJunctionDetailsList();
        allStationList = metroUtilities.getStationList(stationToCode, codeToStation, stationToLines);

        viewPagerFacilitiesParkingAdapter = new ViewPagerFacilitiesParkingAdapter(getSupportFragmentManager(), new ArrayList<>());
        viewPager.setAdapter(viewPagerFacilitiesParkingAdapter);
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

        btnFaciParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String station = edtStation.getText().toString();
                if (station.isEmpty() || !stationToCode.containsKey(station)) {
                    edtStation.setError("Please enter valid station!");
                    Toast.makeText(FacilitiesParking.this, "Please enter valid station!", Toast.LENGTH_SHORT).show();
                    return;
                }

                ProgressDialog progressDialog = new ProgressDialog(FacilitiesParking.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Set the style to spinner
                progressDialog.setCancelable(false); // Make the dialog not cancelable
                progressDialog.show();

                ArrayList<ArrayList<String>> allJunctions = new ArrayList<>();
                ArrayList<String> parkingMetro = new ArrayList<>();
                ArrayList<String> facilitiesMetro = new ArrayList<>();

                for (int i = 0; i < allJunctionList.size(); i++) {
                    if (allJunctionList.get(i).getName().equals(station)) {
                        parkingMetro.add("Available Parking \n" + allJunctionList.get(i).getNearParkingSlots());
                        facilitiesMetro.add("Available Facilities \n" + allJunctionList.get(i).getFacilities());
                        break;
                    }
                }

                allJunctions.add(parkingMetro);
                allJunctions.add(facilitiesMetro);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        viewPager.setVisibility(View.VISIBLE);
                        tabLayout.setVisibility(View.VISIBLE);
                        viewPagerFacilitiesParkingAdapter.updateData(allJunctions);
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
        btnFaciParking = findViewById(R.id.btnFindFaciParking);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        btnBack = findViewById(R.id.btnBack);
    }
}
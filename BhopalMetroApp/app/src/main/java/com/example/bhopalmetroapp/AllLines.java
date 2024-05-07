package com.example.bhopalmetroapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class AllLines extends AppCompatActivity {

    AutoCompleteTextView edtStation;
    ImageButton btnBack;
    TextView txtLine;
    Button btnFindLine;
    TabLayout tabLayout;
    ViewPager viewPager;
    MetroUtilities metroUtilities = new MetroUtilities(AllLines.this);

    ArrayList<String> allStationsList = new ArrayList<>();
    HashMap<String, String> stationToLines = new HashMap<>();
    HashMap<String, Integer> stationToCode = new HashMap<>();
    HashMap<Integer, String> codeToStation = new HashMap<>();

    ViewPagerLineAdapter viewPagerLineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_lines);

        init();

        allStationsList = metroUtilities.getStationList(stationToCode, codeToStation, stationToLines);

        viewPagerLineAdapter = new ViewPagerLineAdapter(getSupportFragmentManager(), new ArrayList<>());
        viewPager.setAdapter(viewPagerLineAdapter);
        tabLayout.setupWithViewPager(viewPager);


        ArrayAdapter<String> stationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, allStationsList);
        edtStation.setAdapter(stationAdapter);
        edtStation.setThreshold(1); // when user enter one character list will be show there
        edtStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtStation.showDropDown();
            }
        });


        btnFindLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String station = edtStation.getText().toString();
                if (station.isEmpty() || !stationToCode.containsKey(station)) {
                    edtStation.setError("Please enter valid station!");
                    Toast.makeText(AllLines.this, "Please enter valid station!", Toast.LENGTH_SHORT).show();
                    txtLine.setVisibility(View.GONE);
                    return;
                }

                txtLine.setText(station + " present in " + stationToLines.get(station));
                txtLine.setVisibility(View.VISIBLE);
            }
        });

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Set the style to spinner
        progressDialog.setCancelable(false); // Make the dialog not cancelable
        progressDialog.show();

        ArrayList<ArrayList<String>> stationsByLine = new ArrayList<>();

        // Iterate over the HashMap to organize stations by line
        for (HashMap.Entry<String, String> entry : stationToLines.entrySet()) {
            String station = entry.getKey();
            String line = entry.getValue();

            // Find the index of the line in the list
            int lineIndex = -1;
            for (int i = 0; i < stationsByLine.size(); i++) {
                if (stationsByLine.get(i).get(0).equals(line)) {
                    lineIndex = i;
                    break;
                }
            }

            // If line not found, create a new list for it
            if (lineIndex == -1) {
                ArrayList<String> newLineList = new ArrayList<>();
                newLineList.add(line);
                newLineList.add(station);
                stationsByLine.add(newLineList);
            } else {
                // Add station to existing line list
                stationsByLine.get(lineIndex).add(station);
            }
        }
        Collections.sort(stationsByLine, Comparator.comparing(list -> list.get(0)));

        // Print the stations organized by line
        for (ArrayList<String> lineList : stationsByLine) {
            System.out.println("Line: " + lineList.get(0));
            for (int i = 1; i < lineList.size(); i++) {
                System.out.println("Station: " + lineList.get(i));
            }
            System.out.println();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                viewPager.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                viewPagerLineAdapter.updateData(stationsByLine);
            }
        }, 1000);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        edtStation = findViewById(R.id.edtStation);
        txtLine = findViewById(R.id.txtLines);
        btnFindLine = findViewById(R.id.btnFindLines);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        btnBack = findViewById(R.id.btnBack);
    }
}
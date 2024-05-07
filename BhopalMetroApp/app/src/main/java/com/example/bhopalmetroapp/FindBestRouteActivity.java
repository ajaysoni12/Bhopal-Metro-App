package com.example.bhopalmetroapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
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

public class FindBestRouteActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    AutoCompleteTextView edtSource, edtDestination;
    Button btnFindRoute;
    ImageButton btnBack;

    ArrayList<String> allStationsList = new ArrayList<>();
    ArrayList<ArrayList<Pair<Integer, Pair<Double, Integer>>>> adjacencyList = new ArrayList<>();
    ArrayList<ArrayList<Pair<Integer, Pair<Double, Double>>>> adjacencyListCost = new ArrayList<>();
    MetroUtilities metroUtilities = new MetroUtilities(FindBestRouteActivity.this);

    HashMap<String, Integer> stationToCode = new HashMap<>();
    HashMap<Integer, String> codeToStation = new HashMap<>();

    ViewPagerRouteAdapter viewPagerRouteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_best_route);

        init();

        allStationsList = metroUtilities.getStationList(stationToCode, codeToStation, new HashMap<>());
        adjacencyList = metroUtilities.getAdjacencyList();
        adjacencyListCost = metroUtilities.getAdjacencyListCost();

        for (int i = 0; i < adjacencyListCost.size(); i++) {
            System.out.print("Station " + i + ": ");
            for (Pair<Integer, Pair<Double, Double>> neighbor : adjacencyListCost.get(i)) {
                int node = neighbor.first;
                double distance = neighbor.second.first;
                double time = neighbor.second.second;
                System.out.print("(Node: " + node + ", Distance: " + distance + ", Time: " + time + ") ");
            }
            System.out.println();
        }

        if (allStationsList.size() == 0) {
            Toast.makeText(this, "something went wrong!!", Toast.LENGTH_SHORT).show();
        }

        viewPagerRouteAdapter = new ViewPagerRouteAdapter(getSupportFragmentManager(), new ArrayList<>());
        viewPager.setAdapter(viewPagerRouteAdapter);
        tabLayout.setupWithViewPager(viewPager);

        ArrayAdapter<String> sourceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, allStationsList);
        ArrayAdapter<String> destinationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, allStationsList);
        edtSource.setAdapter(sourceAdapter);
        edtDestination.setAdapter(destinationAdapter);
        edtSource.setThreshold(1); // when user enter one character list will be show there
        edtDestination.setThreshold(1);

        // user click on drop down menu, all stations list shown
        edtSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSource.showDropDown();
            }
        });
        edtDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtDestination.showDropDown();
            }
        });

        btnFindRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findRoute();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void findRoute() {
        String source = edtSource.getText().toString();
        String destination = edtDestination.getText().toString();

        if (source.isEmpty()) {
            edtSource.setError("Please enter source station!");
            Toast.makeText(this, "Please enter source station!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            edtDestination.setError("Please enter destination station!");
            Toast.makeText(this, "Please enter destination station!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!stationToCode.containsKey(source) || !stationToCode.containsKey(destination)) {
            Toast.makeText(this, "Invalid stations!", Toast.LENGTH_SHORT).show();
            return;
        }

        int sourceStationCode = stationToCode.get(source);
        int destinationStationCode = stationToCode.get(destination);

        System.out.println("Source: " + source);
        System.out.println("Destination: " + destination);


        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Set the style to spinner
        progressDialog.setCancelable(false); // Make the dialog not cancelable
        progressDialog.show();

        ArrayList<ArrayList<String>> allAvailableRoutes = new ArrayList<>();
        // ArrayList<String> shortestDistancePath = metroUtilities.findPathAccordingToDistance(sourceStationCode, destinationStationCode, codeToStation);
        allAvailableRoutes.add(metroUtilities.findPathAccordingToDistance(sourceStationCode, destinationStationCode, codeToStation));
        allAvailableRoutes.add(metroUtilities.findPathAccordingToCost(sourceStationCode, destinationStationCode, codeToStation));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                viewPager.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                viewPagerRouteAdapter.updateData(allAvailableRoutes);
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
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        edtSource = findViewById(R.id.edtSource);
        edtDestination = findViewById(R.id.edtDestination);
        btnFindRoute = findViewById(R.id.btnFindRoute);
        btnBack = findViewById(R.id.btnBack);
    }

}
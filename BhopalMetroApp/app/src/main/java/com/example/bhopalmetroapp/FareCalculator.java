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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.HashMap;

public class FareCalculator extends AppCompatActivity {

    AutoCompleteTextView edtSource, edtDestination;
    Button btnFindTicketFare;
    TextView txtTicketFare;
    CardView cardView;
    ImageButton btnBack;

    ArrayList<String> allStationsList = new ArrayList<>();

    MetroUtilities metroUtilities = new MetroUtilities(FareCalculator.this);

    HashMap<String, Integer> stationToCode = new HashMap<>();
    HashMap<Integer, String> codeToStation = new HashMap<>();
    HashMap<String, String> stationToLine = new HashMap<>();

    ArrayList<ArrayList<Pair<Integer, Pair<Double, Integer>>>> adjacencyList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_calculator);

        init();

        allStationsList = metroUtilities.getStationList(stationToCode, codeToStation, stationToLine);
        adjacencyList = metroUtilities.getAdjacencyList();

        if (allStationsList.size() == 0 || adjacencyList.size() == 0) {
            Toast.makeText(this, "something went wrong!!", Toast.LENGTH_SHORT).show();
            return;
        }

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

        btnFindTicketFare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findTicketFare();
            }
        });

    }

    private void findTicketFare() {
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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                double cost = metroUtilities.findPathAccordingToCost1(sourceStationCode, destinationStationCode, codeToStation) * 3.5;
                // txtTicketFare.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.VISIBLE);
                txtTicketFare.setText(codeToStation.get(sourceStationCode) + " to " + codeToStation.get(destinationStationCode) + " Ticket Fare is: " + cost + "/-");
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
        edtSource = findViewById(R.id.edtSource);
        edtDestination = findViewById(R.id.edtDestination);
        btnFindTicketFare = findViewById(R.id.btnTicketFare);
        txtTicketFare = findViewById(R.id.txtTicketFare);
        btnBack = findViewById(R.id.btnBack);
        cardView = findViewById(R.id.cardView);
    }
}
package com.example.bhopalmetroapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Helplines extends AppCompatActivity {


    RecyclerView rvHelplines;
    ArrayList<helplinesHelp> helpLinesList = new ArrayList<>();

    ImageButton btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helplines);

        rvHelplines = findViewById(R.id.rvHelplines);
        btnBack = findViewById(R.id.btnBack);


        helpLinesList.add(new helplinesHelp(R.drawable.bmp_polic, "Police", "100"));
        helpLinesList.add(new helplinesHelp(R.drawable.bmp_ambulance, "Ambulance", "108"));
        helpLinesList.add(new helplinesHelp(R.drawable.bmp_fire_station, "Fire Station", "101"));
        helpLinesList.add(new helplinesHelp(R.drawable.bmp_women, "Women Helpline", "181"));
        helpLinesList.add(new helplinesHelp(R.drawable.bmp_customer, "Metro Customer Care", "07994693646"));

        System.out.println("Size: " + helpLinesList.size());
        HelplinesAdapter helplinesAdapter = new HelplinesAdapter(Helplines.this, helpLinesList);
        rvHelplines.setLayoutManager(new LinearLayoutManager(this));
        rvHelplines.setAdapter(helplinesAdapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
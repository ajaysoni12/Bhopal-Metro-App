package com.example.bhopalmetroapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class JunctionsActivity extends AppCompatActivity {

    RecyclerView rvJunctions;
    ImageButton btnBack;
    ArrayList<JunctionDetails> junctionDetailsList;
    MetroUtilities metroUtilities = new MetroUtilities(JunctionsActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_junctions);

        rvJunctions = findViewById(R.id.rvJunctions);
        btnBack = findViewById(R.id.btnBack);


        junctionDetailsList = metroUtilities.getJunctionDetailsList();
        if (junctionDetailsList.size() == 0) {
            Toast.makeText(this, "sorry!, currently we don't have any junctions!!", Toast.LENGTH_SHORT).show();
            return;
        }

        JunctionAdapter junctionAdapter = new JunctionAdapter(JunctionsActivity.this, junctionDetailsList);
        rvJunctions.setLayoutManager(new LinearLayoutManager(this));
        rvJunctions.setAdapter(junctionAdapter);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
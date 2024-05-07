package com.example.bhopalmetroapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<JunctionDetails> junctionDetailsList;
    List<ArrayList<Pair<Integer, Pair<String, String>>>> adjacencyList;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

    }

    public void features(View view) {
        int id = view.getId();

        if (id == R.id.cvJunction) {
            Intent iJunction = new Intent(MainActivity.this, JunctionsActivity.class);
            startActivity(iJunction);
        } else if (id == R.id.cvRouteMap) {
            String value = "https://drive.google.com/file/d/1_ur3ujYIQFgc8IYxUKnmNzN0hJ6qHJIb/view";
            Intent iMap = new Intent(Intent.ACTION_VIEW, Uri.parse(value));
            startActivity(iMap);
        } else if (id == R.id.cvFindRoute) {
            Intent iRoute = new Intent(MainActivity.this, FindBestRouteActivity.class);
            startActivity(iRoute);
        } else if (id == R.id.cvSeeLines) {
            Intent iLines = new Intent(MainActivity.this, AllLines.class);
            startActivity(iLines);
        } else if (id == R.id.cvFLMetro) {
            Intent iMetro = new Intent(MainActivity.this, FirstLastMetro.class);
            startActivity(iMetro);
        } else if (id == R.id.cvUpMetro) {
            Intent iUpMetro = new Intent(MainActivity.this, UpcomingMetro.class);
            startActivity(iUpMetro);
        } else if (id == R.id.cvFacilities) {
            Intent iFaciPark = new Intent(MainActivity.this, FacilitiesParking.class);
            startActivity(iFaciPark);
        } else if (id == R.id.cvTrainTime) {
            Intent iTrainTime = new Intent(MainActivity.this, TrainTime.class);
            startActivity(iTrainTime);
        } else if (id == R.id.cvFareCal) {
            Intent iFare = new Intent(MainActivity.this, FareCalculator.class);
            startActivity(iFare);
        } else if (id == R.id.cvHelpLines) {
            Intent iHelplines = new Intent(MainActivity.this, Helplines.class);
            startActivity(iHelplines);
        } else if (id == R.id.cvInteractiveMap) {
            Intent iMapp = new Intent(MainActivity.this, InteractiveMap.class);
            startActivity(iMapp);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_search) {
            Toast.makeText(this, "Click Search Icon.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_settings) {
            Intent iDev = new Intent(MainActivity.this, About_Developer.class);
            startActivity(iDev);
        }

        return super.onOptionsItemSelected(item);
    }
}
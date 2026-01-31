package com.example.bhopalmetroapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavoritesActivity extends AppCompatActivity {

    RecyclerView rvFavorites;
    TextView tvEmpty;
    FavoritesAdapter adapter;
    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        rvFavorites = findViewById(R.id.rvFavorites);
        tvEmpty = findViewById(R.id.tvEmpty);

        rvFavorites.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavoritesAdapter(this, new ArrayList<>() , stationName -> {
            Intent i = new Intent(FavoritesActivity.this, TrainTime.class);
            i.putExtra("station_name", stationName);
            startActivity(i);
        });
        rvFavorites.setAdapter(adapter);

        loadFavorites();
    }

    private void loadFavorites() {
        executor.execute(() -> {
            List<FavoriteStation> favs = AppDatabase.getInstance(this).favoriteDao().getAll();
            runOnUiThread(() -> {
                adapter.setData(favs);
                tvEmpty.setVisibility(favs.isEmpty() ? View.VISIBLE : View.GONE);
            });
        });
    }
}

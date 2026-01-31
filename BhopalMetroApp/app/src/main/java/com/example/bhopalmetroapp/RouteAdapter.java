package com.example.bhopalmetroapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {

    private ArrayList<String> routeList;
    private Context context;
    private MetroUtilities metroUtilities;
    private HashMap<String, Integer> stationToCode = new HashMap<>();
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public RouteAdapter(Context context, ArrayList<String> dataList) {
        this.context = context;
        this.routeList = dataList;
        this.metroUtilities = new MetroUtilities(context);
        metroUtilities.getStationList(stationToCode, new HashMap<Integer, String>(), new HashMap<String, String>());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String stationName = routeList.get(position);
        holder.textView.setText(stationName);

        // Open TrainTime on row click
        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, TrainTime.class);
            i.putExtra("station_name", stationName);
            context.startActivity(i);
        });

        // Favorite state (background)
        Integer code = stationToCode.get(stationName);
        if (code != null) {
            executor.execute(() -> {
                AppDatabase db = AppDatabase.getInstance(context);
                boolean fav = db.favoriteDao().exists(code) > 0;
                holder.itemView.post(() -> holder.btnFavorite.setImageResource(fav ? android.R.drawable.btn_star_big_on : android.R.drawable.btn_star_big_off));

                // ETA compute
                ArrayList<ArrayList<String>> at = metroUtilities.getArrivalDepartureTime(code);
                String etaText = "--";
                if (at.size() >= 2 && at.get(1).size() > 0) {
                    String next = null;
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);
                        Calendar now = Calendar.getInstance();
                        for (String t : at.get(1)) {
                            Date d = sdf.parse(t);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(d);
                            cal.set(Calendar.YEAR, now.get(Calendar.YEAR));
                            cal.set(Calendar.DAY_OF_YEAR, now.get(Calendar.DAY_OF_YEAR));

                            if (cal.getTimeInMillis() >= now.getTimeInMillis()) {
                                next = t;
                                long diff = (cal.getTimeInMillis() - now.getTimeInMillis()) / 60000; // minutes
                                etaText = String.format(Locale.US, "Next: %s (%dm)", t, diff);
                                break;
                            }
                        }
                        if (next == null) {
                            etaText = "No more today";
                        }
                    } catch (ParseException e) {
                        etaText = "--";
                    }
                }
                final String finalEta = etaText;
                holder.itemView.post(() -> holder.txtEta.setText(finalEta));
            });
        } else {
            holder.txtEta.setText("--");
            holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_off);
        }

        // Toggle favorite
        holder.btnFavorite.setOnClickListener(v -> {
            if (code == null) {
                Toast.makeText(context, "Station code unavailable", Toast.LENGTH_SHORT).show();
                return;
            }
            executor.execute(() -> {
                AppDatabase db = AppDatabase.getInstance(context);
                if (db.favoriteDao().exists(code) > 0) {
                    db.favoriteDao().deleteByCode(code);
                    holder.itemView.post(() -> {
                        holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_off);
                        Toast.makeText(context, context.getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT).show();
                    });
                } else {
                    FavoriteStation fav = new FavoriteStation();
                    fav.stationCode = code;
                    fav.stationName = stationName;
                    fav.addedAt = System.currentTimeMillis();
                    db.favoriteDao().insert(fav);
                    holder.itemView.post(() -> {
                        holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_on);
                        Toast.makeText(context, context.getString(R.string.added_to_favorites), Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView txtEta;
        ImageButton btnFavorite;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            txtEta = itemView.findViewById(R.id.txtEta);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
        }
    }
}

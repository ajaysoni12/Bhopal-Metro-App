package com.example.bhopalmetroapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    private List<FavoriteStation> data = new ArrayList<>();
    private Context context;
    private OnClickListener listener;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public interface OnClickListener {
        void onOpen(String stationName);
    }

    public FavoritesAdapter(Context context, List<FavoriteStation> data, OnClickListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    public void setData(List<FavoriteStation> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteStation f = data.get(position);
        holder.textView.setText(f.stationName);
        holder.btnFavorite.setImageResource(android.R.drawable.btn_star_big_on);

        holder.itemView.setOnClickListener(v -> listener.onOpen(f.stationName));
        holder.btnFavorite.setOnClickListener(v -> {
            executor.execute(() -> {
                AppDatabase.getInstance(context).favoriteDao().deleteByCode(f.stationCode);
                ((FavoritesActivity) context).runOnUiThread(() -> {
                    data.remove(position);
                    notifyItemRemoved(position);
                });
            });
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView txtEta;
        ImageButton btnFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            txtEta = itemView.findViewById(R.id.txtEta);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
        }
    }
}

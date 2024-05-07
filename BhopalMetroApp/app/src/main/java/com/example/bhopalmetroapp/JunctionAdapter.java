package com.example.bhopalmetroapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JunctionAdapter extends RecyclerView.Adapter<JunctionAdapter.ViewHolder> {

    Context context;
    List<JunctionDetails> junctionDetailsList;

    public JunctionAdapter(Context context, List<JunctionDetails> junctionDetailsList) {
        this.context = context;
        this.junctionDetailsList = junctionDetailsList;
    }

    @NonNull
    @Override
    public JunctionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.junction_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JunctionAdapter.ViewHolder holder, int position) {
        holder.txtJunctionName.setText(junctionDetailsList.get(position).getName());

        StringBuilder linesBuilder = new StringBuilder();
        int cnt = 1;
        for (String line : junctionDetailsList.get(position).getLines()) {
            linesBuilder.append("(" + cnt + ") " + line).append("\n");
            cnt++;
        }
        String lines = linesBuilder.toString();
        // Remove the trailing comma and space
        lines = lines.isEmpty() ? "" : lines.substring(0, lines.length() - 2);
        holder.txtJunctionLines.setText(lines);

        holder.imgDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int is_vis = holder.txtJunctionLines.getVisibility();
                if (is_vis == View.VISIBLE) {
                    holder.imgDropDown.setImageResource(R.drawable.ic_down);
                    holder.txtAvailableLines.setText("click on down button for knowing this junctions present on which lines");
                    holder.txtJunctionLines.setVisibility(View.GONE);
                } else {
                    holder.imgDropDown.setImageResource(R.drawable.ic_up);
                    holder.txtAvailableLines.setText("available at lines");
                    holder.txtJunctionLines.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return junctionDetailsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtJunctionName, txtJunctionLines, txtAvailableLines;
        ImageView imgDropDown;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtJunctionName = itemView.findViewById(R.id.txtJunctionName);
            txtJunctionLines = itemView.findViewById(R.id.txtJunctionLines);
            imgDropDown = itemView.findViewById(R.id.imgDropDown);
            txtAvailableLines = itemView.findViewById(R.id.txtAvailableLines);
        }
    }
}

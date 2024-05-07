package com.example.bhopalmetroapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HelplinesAdapter extends RecyclerView.Adapter<HelplinesAdapter.ViewHolder> {

    Context context;
    ArrayList<helplinesHelp> helplinesList;

    public HelplinesAdapter(Context context, ArrayList<helplinesHelp> helplinesList) {
        this.context = context;
        this.helplinesList = helplinesList;
    }

    @NonNull
    @Override
    public HelplinesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_helpline, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HelplinesAdapter.ViewHolder holder, int position) {

        holder.imageLogo.setImageResource(helplinesList.get(position).getLogoImage());
        holder.txtMobileNumber.setText(helplinesList.get(position).getMobileNumber());
        holder.txtLogoName.setText(helplinesList.get(position).getLogoName());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = holder.txtMobileNumber.getText().toString().trim();
                openDialer(phoneNumber);
            }
        });

    }

    @Override
    public int getItemCount() {
        return helplinesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageLogo;
        public TextView txtLogoName;
        public TextView txtMobileNumber;

        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageLogo = itemView.findViewById(R.id.imageLogo);
            txtLogoName = itemView.findViewById(R.id.txtLogoName);
            txtMobileNumber = itemView.findViewById(R.id.txtMobileNumber);
            linearLayout = itemView.findViewById(R.id.llView);
        }
    }

    private void openDialer(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }
}

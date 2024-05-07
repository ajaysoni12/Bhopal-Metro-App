package com.example.bhopalmetroapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class About_Developer extends AppCompatActivity {

    TextView txtContactInfo;

    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developer);

        txtContactInfo = findViewById(R.id.txtContactInfo);
        btnBack = findViewById(R.id.btnBack);

        txtContactInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonNo = txtContactInfo.getText().toString().trim();
                openDialer(phonNo);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void openDialer(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}
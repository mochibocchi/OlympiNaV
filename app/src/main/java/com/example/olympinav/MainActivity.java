package com.example.olympinav;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActivity();

        FloatingActionButton fabAddTicket = findViewById(R.id.fabAddTicket);
        fabAddTicket.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ScanQRCodeActivity.class)));

        FloatingActionButton fabPlanTrip = findViewById(R.id.fabPlanTrip);
        fabPlanTrip.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PlanTripActivity.class)));
    }
}
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ScanQRCodeActivity.class)));

        findViewById(R.id.event).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PlanTripActivity.class)));
    }
}
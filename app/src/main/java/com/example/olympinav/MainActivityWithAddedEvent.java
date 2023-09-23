package com.example.olympinav;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivityWithAddedEvent extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_with_added_event);
        setupActivity();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> startActivity(new Intent(MainActivityWithAddedEvent.this, ScanQRCodeActivity.class)));

        findViewById(R.id.event).setOnClickListener(v -> startActivity(new Intent(MainActivityWithAddedEvent.this, ViewTripActivity.class)));
        findViewById(R.id.event).setOnClickListener(v -> startActivity(new Intent(MainActivityWithAddedEvent.this, PlanTripActivity.class)));
    }
}
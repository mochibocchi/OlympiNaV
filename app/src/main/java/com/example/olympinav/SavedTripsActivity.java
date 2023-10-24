package com.example.olympinav;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

public class SavedTripsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_trips);
        setupActivity();
        Button addTripButton = findViewById(R.id.addTripButton);

        addTripButton.setOnClickListener(view -> startActivity(new Intent(SavedTripsActivity.this, PlanTripActivity.class)));

    }
}

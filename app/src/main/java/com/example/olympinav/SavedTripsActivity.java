package com.example.olympinav;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SavedTripsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_trips);
        setupActivity();
        FloatingActionButton backFab = findViewById(R.id.backButton);
        ImageButton helpButton = findViewById(R.id.helpButton);

        // Press back button to exit out of activity
        backFab.setOnClickListener(view -> finish());
        helpButton.setOnClickListener(view -> finish());

    }
}

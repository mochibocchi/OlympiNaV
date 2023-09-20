package com.example.olympinav;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

public class SavedTripsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_trips);
        setupActivity();
        ImageButton backButton = findViewById(R.id.backButton);

        // Press back button to exit out of activity
        backButton.setOnClickListener(view -> finish());
    }
}

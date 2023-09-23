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
        ImageButton helpButton = findViewById(R.id.helpButton);

        helpButton.setOnClickListener(view -> finish());

    }
}

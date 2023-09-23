package com.example.olympinav;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class EventDetailsActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventdetails);
        setupActivity();

        Button planTripButton = findViewById(R.id.planTripButton);

        // Set an OnClickListener to start PlanTripActivity
        planTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventDetailsActivity.this, PlanTripActivity.class));
            }
        });
    }
}
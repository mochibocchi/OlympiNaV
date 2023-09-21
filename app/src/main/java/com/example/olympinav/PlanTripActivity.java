package com.example.olympinav;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;

public class PlanTripActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_trip);
        setupActivity();

        Spinner tripTypeSpinner = findViewById(R.id.tripTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.plan_trip_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tripTypeSpinner.setAdapter(adapter);

        View.OnClickListener l = v -> startActivity(new Intent(PlanTripActivity.this, ViewTripActivity.class));

        findViewById(R.id.card1).setOnClickListener(l);
        findViewById(R.id.card2).setOnClickListener(l);
    }
}

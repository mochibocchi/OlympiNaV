package com.example.olympinav;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class FeedbackActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        setupActivity("Feedback");
        setupViews();
    }

    private void setupViews() {
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        // The comment edit text is not needed for validation purposes.
        Button submitButton = findViewById(R.id.btnSubmit);

        submitButton.setOnClickListener(v -> {
            if (ratingBar.getRating() == 0) {
                Toast.makeText(this, "Please select a star to provide us with a 1-5 rating", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Thank you for your feedback", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(FeedbackActivity.this, MainActivity.class));
        });
    }
}

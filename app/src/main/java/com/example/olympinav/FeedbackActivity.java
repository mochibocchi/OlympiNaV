package com.example.olympinav;

import android.os.Bundle;

import androidx.annotation.Nullable;

public class FeedbackActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        setupActivity();
    }
}
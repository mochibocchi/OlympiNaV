package com.example.olympinav;

import android.os.Bundle;

import androidx.annotation.Nullable;

public class ConfirmTicketActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_ticket);
        setupActivity();
    }
}

package com.example.olympinav;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class ConfirmTicketActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_ticket);
        setupActivity();

        findViewById(R.id.backButton).setOnClickListener(v -> startActivity(new Intent(ConfirmTicketActivity.this,
                MainActivity.class)));
        findViewById(R.id.okButton).setOnClickListener(v -> startActivity(new Intent(ConfirmTicketActivity.this,
                MainActivity.class)));
    }
}

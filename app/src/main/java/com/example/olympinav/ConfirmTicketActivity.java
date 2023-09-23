package com.example.olympinav;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

public class ConfirmTicketActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_ticket);
        setupActivity();

        Button btn = findViewById(R.id.okButton);
        btn.setOnClickListener(v -> startActivity(new Intent(ConfirmTicketActivity.this, MainActivityWithAddedEvent.class)));
    }
}

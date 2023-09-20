package com.example.olympinav;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

public class ScanQRCodeActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        setupActivity();
        ImageButton backButton = findViewById(R.id.backButton);
        Button addTicketBtn = findViewById(R.id.addTicketButton);

        // Press add ticket button to navigate to ConfirmTicket activity
        addTicketBtn.setOnClickListener(v -> startActivity(new Intent(ScanQRCodeActivity.this, ConfirmTicketActivity.class)));
        // Press back button to exit out of activity
        backButton.setOnClickListener(view -> finish());
    }
}

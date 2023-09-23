package com.example.olympinav;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

public class ScanQRCodeActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        setupActivity();
        Button addTicketBtn = findViewById(R.id.addTicketButton);

        // Press add ticket button to navigate to ConfirmTicket activity
        addTicketBtn.setOnClickListener(v -> startActivity(new Intent(ScanQRCodeActivity.this, ConfirmTicketActivity.class)));
    }
}

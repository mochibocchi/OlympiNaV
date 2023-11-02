package com.example.olympinav;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olympinav.DB.Event;
import com.example.olympinav.DB.EventDao;
import com.example.olympinav.DB.Ticket;
import com.example.olympinav.DB.TicketWithEvent;
import com.example.olympinav.Utils.MyApp;

public class EventDetailsActivity extends BaseActivity {

    TextView tvTicketId, tvEventName, tvEventDate, tvEventAddress, tvImageId;
    Button btnUpdate, btnDelete;
    EventDao eventDao;
    Event event;
    private Ticket ticket;
    ImageView eventImage;
    public static final String EXTRA_START_LOCATION = "start_location";
    public static final String EXTRA_END_LOCATION = "end_location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        setupActivity("View Ticket");

        tvTicketId = findViewById(R.id.tvTicketId);
        tvEventName = findViewById(R.id.tvEventName);
        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventAddress = findViewById(R.id.tvEventAddress);
        tvImageId = findViewById(R.id.tvImageId);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        eventImage = findViewById(R.id.eventImageView);

        eventDao = MyApp.getAppDatabase().eventDao();

        long ticketId = getIntent().getLongExtra("ticketId", -1);
        if (ticketId == -1) {
            Toast.makeText(this, "An unknown error occurred ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(EventDetailsActivity.this, MainActivity.class));
            return;
        }

        for (TicketWithEvent ticketWithEvent : MyApp.getUser().getTickets()) {
            if (ticketWithEvent.getTicket().getId().equals(ticketId)) {
                ticket = ticketWithEvent.getTicket();
                event = ticketWithEvent.getEvent();
            }
        }

        if (event == null) {
            Toast.makeText(this, "Couldn't find your ticket for this event, please try again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(EventDetailsActivity.this, MainActivity.class));
        }

        tvTicketId.setText(this.event.getTicketId());
        tvEventName.setText(this.event.getEventName());
        tvEventDate.setText(this.event.getDate());
        tvEventAddress.setText(this.event.getAddress());
        tvImageId.setText(this.event.getImageId());
        eventImage.setImageResource(event.getImageId());

        btnUpdate.setOnClickListener(view -> {
            if (event != null) {
                openPlanTripActivity();
            }
        });
        btnDelete.setOnClickListener(view -> {
            if (event != null) {
                deleteTicket();
            }
        });
    }

    private void openPlanTripActivity() {
        Intent intent = new Intent(EventDetailsActivity.this, PlanTripActivity.class);
        String eventAddress = event.getAddress();
        String startLocation = "Current Location";
        intent.putExtra(EXTRA_START_LOCATION, startLocation);
        intent.putExtra(EXTRA_END_LOCATION, eventAddress);
        startActivity(intent);
    }

    private void deleteTicket() {
        AsyncTask.execute(() -> {
            // Delete the event from the database
            MyApp.getAppDatabase().ticketDao().delete(ticket);
            MyApp.setUser(MyApp.getAppDatabase().userDao().getUserWithTicketsAndEvents(MyApp.getUser().getUser().getUsername()));

            // Navigate back to the MainActivity
            runOnUiThread(() -> {
                Toast.makeText(this, "Ticket Successfully Deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            });
        });
    }

}
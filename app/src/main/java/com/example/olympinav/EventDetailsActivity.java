package com.example.olympinav;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.olympinav.DB.Event;
import com.example.olympinav.DB.EventDao;
import com.example.olympinav.Utils.MyApp;

import java.util.HashSet;
import java.util.Set;

public class EventDetailsActivity extends BaseActivity {

    TextView tvTicketId, tvEventName, tvEventDate, tvEventAddress, tvImageId;
    Button btnUpdate, btnDelete;
    EventDao eventDao;
    int eventId;
    Event event;
    ImageView eventImage;
    private static final String SHARED_PREF_KEY = "MyPreferences";
    private Set<String> enteredTicketNumbers = new HashSet<>();
    public static final String EXTRA_START_LOCATION = "start_location";
    public static final String EXTRA_END_LOCATION = "end_location";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        setupActivity();

        SharedPreferences preferences = getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        enteredTicketNumbers = preferences.getStringSet("enteredTicketNumbers", new HashSet<>());

        tvTicketId = findViewById(R.id.tvTicketId);
        tvEventName = findViewById(R.id.tvEventName);
        tvEventDate = findViewById(R.id.tvEventDate);
        tvEventAddress = findViewById(R.id.tvEventAddress);
        tvImageId = findViewById(R.id.tvImageId);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        eventImage = findViewById(R.id.eventImageView);

        eventDao = MyApp.getAppDatabase().eventDao();

        if (getIntent().getIntExtra("eventId", -1) != -1){
            eventId = getIntent().getIntExtra("eventId", -1);

            eventDao.getEventById(eventId).observe(this, dbEvent -> {
                if (dbEvent != null) {
                    this.event = dbEvent;
                    tvTicketId.setText(this.event.getTicketId());
                    tvEventName.setText(this.event.getEventName());
                    tvEventDate.setText(this.event.getDate());
                    tvEventAddress.setText(this.event.getAddress());
                    tvImageId.setText(this.event.getImageId());
                    eventImage.setImageResource(event.getImageId());
                }
            });

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (event != null) {
                        openPlanTripActivity();
                    }
                }

            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (event != null) {
                        deleteTheEvent();
                        Toast.makeText(EventDetailsActivity.this,"Delete", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else {
            Toast.makeText(this, "No Event Id Found", Toast.LENGTH_SHORT).show();
        }

    }

    private void openPlanTripActivity() {
        Intent intent = new Intent(EventDetailsActivity.this, PlanTripActivity.class);
        String eventAddress = event.getAddress();
        String startLocation = "Current Location";
        intent.putExtra(EXTRA_START_LOCATION, startLocation);
        intent.putExtra(EXTRA_END_LOCATION, eventAddress);
        startActivity(intent);
    }
    private void updateTheEvent() {

        // Creating the view to create the dialog. We are re-using the dialog we created in Week-4 to add new event.
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_event, null);
        EditText editTextEventName = dialogView.findViewById(R.id.editTextEventName);
        EditText editTextEventDate = dialogView.findViewById(R.id.editTextEventDate);

        // Pre-set the current event name and type to these edittext views.
        editTextEventName.setText(event.getEventName());
        editTextEventDate.setText(event.getDate());

        //Creating the dialog builder to create the pop up dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        //Setting and implementing the update operation
        builder.setPositiveButton("Update", (dialog, which) -> {
            String eventName = editTextEventName.getText().toString();
            String eventType = editTextEventDate.getText().toString();

            //update the values
            event.setEventName(eventName);
            event.setDate(eventType);

            AsyncTask.execute(() -> {
                eventDao.update(event);
            });
        });

        // Setting the update cancellation
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // Do nothing or handle any other actions
            dialog.cancel();
        });

        //Creating and showing the dialog.
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void deleteTheEvent() {
        AsyncTask.execute(() -> {
            // Remove the event's ticket number from the enteredTicketNumbers set
            enteredTicketNumbers.remove(event.getTicketId());

            // Delete the event from the database
            eventDao.delete(event);

            // Navigate back to the MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        Toast.makeText(this, "Delete event" + event.getTicketId(), Toast.LENGTH_SHORT).show();
    }

}
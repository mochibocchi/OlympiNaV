package com.example.olympinav;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.olympinav.DB.Event;
import com.example.olympinav.DB.EventDao;
import com.example.olympinav.Utils.MyApp;

public class EventDetailsActivity extends AppCompatActivity {

    TextView tvTicketId, tvEventName, tvEventDate, tvImageId;
    Button btnUpdate, btnDelete;
    EventDao eventDao;
    int eventId;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        tvTicketId = findViewById(R.id.tvTicketId);
        tvEventName = findViewById(R.id.tvEventName);
        tvEventDate = findViewById(R.id.tvEventDate);
        tvImageId = findViewById(R.id.tvImageId);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        eventDao = MyApp.getAppDatabase().eventDao();

        if (getIntent().getIntExtra("eventId", -1) != -1){
            eventId = getIntent().getIntExtra("eventId", -1);

            eventDao.getEventById(eventId).observe(this, dbEvent -> {
                if (dbEvent != null) {
                    this.event = dbEvent;
                    tvTicketId.setText("Ticket ID: " + this.event.getTicketId());
                    tvEventName.setText("Event Name: " + this.event.getEventName());
                    tvEventDate.setText("Event Date: " + this.event.getDate());
                    tvImageId.setText("Image ID: " + this.event.getImageId());
                }
            });

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (event != null) {
                        updateTheEvent();
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
            eventDao.delete(event);
            finish();
        });
    }

}
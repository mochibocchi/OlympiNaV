package com.example.olympinav;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.olympinav.DB.Event;
import com.example.olympinav.DB.EventDao;
import com.example.olympinav.Utils.MyApp;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    ArrayList<Event> eventList;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddNewTicket;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActivity();

        FloatingActionButton fabPlanTrip = findViewById(R.id.fabPlanTrip);
        fabPlanTrip.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PlanTripActivity.class)));

        FloatingActionButton fabAddTicket = findViewById(R.id.fabAddTicket);

        recyclerView = findViewById(R.id.recyclerView);
        fabAddNewTicket = findViewById(R.id.fabAddTicket);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initialiseEventList();
        eventList = new ArrayList<>();
//        getDataFromDatabase();

        eventAdapter = new EventAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);

        eventAdapter.setOnItemClickListener(position -> {
            Toast.makeText(MainActivity.this,
                    eventList.get(position).getEventName(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, EventDetailsActivity.class);
            Event event = eventList.get(position);
            intent.putExtra("eventId", event.id);
            startActivity(intent);
        });

        fabAddNewTicket.setOnClickListener(view -> {
//          manageNewEventFunctionality();

            AddTicketNumber();
        });
    }

    private void AddTicketNumber() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_get_ticket_number, null);
        builder.setView(dialogView);

        EditText editTextTicketNumber = dialogView.findViewById(R.id.editTextTicketNumber);

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            String ticketNumber = editTextTicketNumber.getText().toString();

            // Fetch and display the event associated with the entered ticket number
            DisplayEvent(ticketNumber);
        });

        AlertDialog dialog = builder.create();
        builder.setNegativeButton("Cancel", null);
        dialog.show();
    }

    private void DisplayEvent(String ticketNumber) {
        EventDao eventDao = MyApp.getAppDatabase().eventDao();

        AsyncTask.execute(() -> {
            Event event = eventDao.getEventByTicketId(ticketNumber);

            if (event != null) {
                runOnUiThread(() -> {
                    eventList.add(event);
                    eventAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Successfully added new ticket " + ticketNumber, Toast.LENGTH_SHORT).show();
                });
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Ticket not found", Toast.LENGTH_SHORT).show();
                    Log.e("Event Retrieval", "Ticket not found: " + ticketNumber);
                });
            }
        });
    }

//    private void manageNewEventFunctionality() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_event, null);
//        builder.setView(dialogView);
//
//        EditText editTextEventName = dialogView.findViewById(R.id.editTextEventName);
//        EditText editTextEventType = dialogView.findViewById(R.id.editTextEventType);
//
//        builder.setPositiveButton("Add", (dialog, which) -> {
//            String eventName = editTextEventName.getText().toString();
//            String eventDate = editTextEventType.getText().toString();
//
//            // Add the new event to the list
//            Event event = new Event(eventName, eventDate);
//
//            EventDao eventDao = MyApp.getAppDatabase().eventDao();
//            AsyncTask.execute(() -> {
//                eventDao.insert(event);
//            });
//
//            // Refresh the RecyclerView
//            //eventAdapter.notifyDataSetChanged();
//        });
//
//        AlertDialog dialog = builder.create();
//        builder.setNegativeButton("Cancel", null);
//        dialog.show();
//    }

    private void getDataFromDatabase() {
//        EventDao eventDao = MyApp.getAppDatabase().eventDao();
//        LiveData<List<Event>> eventsLiveData = eventDao.getAllEvents();
//        eventsLiveData.observe(this, events -> {
//            eventList.clear();
//            eventList.addAll(events);
//            eventAdapter.notifyDataSetChanged();
//        });
    }

    private void initialiseEventList() {
        EventDao eventDao = MyApp.getAppDatabase().eventDao();

        // Create a list of predetermined events here
        List<Event> events = new ArrayList<>();
        events.add(new Event("12345","Swimming Event", "26th January, 8AM", R.drawable.olympic_swimming));
        events.add(new Event("123456","Triathlon Event", "26th January, 12PM", R.drawable.olympic_triathlon));

        // Insert predetermined events into the database
        AsyncTask.execute(() -> {
            for (Event event : events) {
                eventDao.insert(event);
            }
        });
    }
}
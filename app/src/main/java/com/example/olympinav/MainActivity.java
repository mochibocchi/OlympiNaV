package com.example.olympinav;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends BaseActivity {
    ArrayList<Event> eventList;
    Set<String> enteredTicketNumbers = new HashSet<>();
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

        // Load previously user-inputted tickets from a previous session
        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        enteredTicketNumbers = preferences.getStringSet("enteredTicketNumbers", new HashSet<>());
        displayPreviouslyEnteredEvents();

        // Initialise the recycler view for all our event items
        recyclerView = findViewById(R.id.recyclerView);
        fabAddNewTicket = findViewById(R.id.fabAddTicket);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initialiseEventList();
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);

        // If user clicks on any of the event items, it will bring event details activity.
        eventAdapter.setOnItemClickListener(position -> {
            Toast.makeText(MainActivity.this,
                    eventList.get(position).getEventName(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, EventDetailsActivity.class);
            Event event = eventList.get(position);
            intent.putExtra("eventId", event.id);
            startActivity(intent);
        });

        // Click on the + button to add new ticket
        fabAddNewTicket.setOnClickListener(view -> {
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
            // Check if user is entering in a duplicate ticket ID
            if (enteredTicketNumbers.contains(ticketNumber))
            {
                Toast.makeText(this, "Ticket already entered", Toast.LENGTH_SHORT).show();
            }
            else {
                enteredTicketNumbers.add(ticketNumber);

                // Fetch and display the event associated with the entered ticket number
                DisplayEvent(ticketNumber);
            }
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
                    saveEnteredTicketNumbers();
                });
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Ticket not found", Toast.LENGTH_SHORT).show();
                    Log.e("Event Retrieval", "Ticket not found: " + ticketNumber);
                });
            }
        });
    }

    private void initialiseEventList() {

        EventDao eventDao = MyApp.getAppDatabase().eventDao();

        // Pre-populate events into the database
        List<Event> events = new ArrayList<>();
        events.add(new Event("SWM123","Swimming Event", "26th January, 8AM", R.drawable.olympic_swimming));
        events.add(new Event("TRI123","Triathlon Event", "26th January, 12PM", R.drawable.olympic_triathlon));

        AsyncTask.execute(() -> {
            for (Event event : events) {
                eventDao.insert(event);
            }
        });
    }

    private void saveEnteredTicketNumbers() {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Set<String> uniqueTicketNumbers = new HashSet<>(enteredTicketNumbers);
        editor.putStringSet("enteredTicketNumbers", uniqueTicketNumbers);
        editor.apply();
    }

    private void displayPreviouslyEnteredEvents() {
        for (String ticketNumber : enteredTicketNumbers) {
            DisplayEvent(ticketNumber);
        }
    }

    // Idk if we'll need this code later, so I'll just leave it here commented.
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

//    private void getDataFromDatabase() {
//        EventDao eventDao = MyApp.getAppDatabase().eventDao();
//        LiveData<List<Event>> eventsLiveData = eventDao.getAllEvents();
//        eventsLiveData.observe(this, events -> {
//            eventList.clear();
//            eventList.addAll(events);
//            eventAdapter.notifyDataSetChanged();
//        });
//    }
}
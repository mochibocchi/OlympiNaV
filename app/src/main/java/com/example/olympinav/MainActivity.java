package com.example.olympinav;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.olympinav.DB.Event;
import com.example.olympinav.DB.EventDao;
import com.example.olympinav.DB.Ticket;
import com.example.olympinav.DB.TicketWithEvent;
import com.example.olympinav.Utils.MyApp;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    ArrayList<TicketWithEvent> eventList;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddNewTicket;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupActivity("Home");
        setupViews();
        initialiseRecyclerView();
        displayUsersTickets();
    }

    private void initialiseRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initialiseEventList();
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);

        // If user clicks on any of the event items, it will bring event details activity.
        eventAdapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(MainActivity.this, EventDetailsActivity.class);
            TicketWithEvent te = eventList.get(position);
            intent.putExtra("ticketId", te.getTicket().getId());
            startActivity(intent);
        });
    }

    private void setupViews() {
        FloatingActionButton fabPlanTrip = findViewById(R.id.fabPlanTrip);
        fabPlanTrip.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, PlanTripActivity.class)));
        recyclerView = findViewById(R.id.recyclerView);
        fabAddNewTicket = findViewById(R.id.fabAddTicket);
        // Click on the + button to add new ticket
        fabAddNewTicket.setOnClickListener(view -> addTicketNumber());
    }

    private void addTicketNumber() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_get_ticket_number, null);
        builder.setView(dialogView);

        EditText editTextTicketNumber = dialogView.findViewById(R.id.editTextTicketNumber);

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            String ticketNumber = editTextTicketNumber.getText().toString();

            // Check if user has already entered this ticket
            for (TicketWithEvent ticket : MyApp.getUser().getTickets()) {
                if (ticket.getEvent().getTicketId().equals(ticketNumber)) {
                    Toast.makeText(this, "You have already added this ticket.", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            AsyncTask.execute(() -> {
                Event newEvent = MyApp.getAppDatabase().eventDao().getEventByTicketId(ticketNumber);
                if (newEvent == null) {
                    runOnUiThread(() -> Toast.makeText(this, "No Event found with that code", Toast.LENGTH_SHORT).show());
                    return;
                }

                // Insert new ticket
                Ticket ticket = new Ticket(newEvent.getId(), MyApp.getUser().getUser().getUsername());
                MyApp.getAppDatabase().ticketDao().insert(ticket);

                // Update User object.
                MyApp.setUser(MyApp.getAppDatabase().userDao().getUserWithTicketsAndEvents(MyApp.getUser().getUser().getUsername()));

                // Call displayUsersTickets to refresh the UI
                displayUsersTickets();

                runOnUiThread(() -> {
                  clearRecyclerView();
                  displayUsersTickets();
                });
            });
        });

        AlertDialog dialog = builder.create();
        builder.setNegativeButton("Cancel", null);
        dialog.show();
    }

    private void initialiseEventList() {

        EventDao eventDao = MyApp.getAppDatabase().eventDao();

        // Pre-populate events into the database
        List<Event> events = new ArrayList<>();
        events.add(new Event("SWM123","Swimming Event", "26th January, 8AM", "350 Main Beach Road, Brisbane City", R.drawable.olympic_swimming));
        events.add(new Event("TRI123","Triathlon Event", "26th January, 12PM","95 Lemke Road, Brisbane City", R.drawable.olympic_triathlon));

        AsyncTask.execute(() -> {
            for (Event event : events) {
                // Check if an event with the same properties exists in the database
                Event existingEvent = eventDao.getEventByTicketId(event.getTicketId());
                if (existingEvent == null) {
                    eventDao.insert(event);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        clearRecyclerView();
        displayUsersTickets();
    }
    private void displayUsersTickets() {
        if (eventList.isEmpty()) {
            // Event list is empty, show the "No Events Added" message
            TextView noEventsTextView = findViewById(R.id.noEventsTextView);
            noEventsTextView.setVisibility(View.VISIBLE);
        } else {
            // Event list is not empty, hide the "No Events Added" message
            TextView noEventsTextView = findViewById(R.id.noEventsTextView);
            noEventsTextView.setVisibility(View.GONE);

            // Clear the existing eventList
            eventList.clear();

            // Populate the RecyclerView with events
            for (TicketWithEvent ticket : MyApp.getUser().getTickets()) {
                eventList.add(ticket);
            }

            eventAdapter.notifyDataSetChanged();
        }
    }


    private void clearRecyclerView() {
        eventList.clear();
        eventAdapter.notifyDataSetChanged();
    }
}



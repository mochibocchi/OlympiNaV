package com.example.olympinav;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
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
        fabAddTicket.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ScanQRCodeActivity.class)));

        recyclerView = findViewById(R.id.recyclerView);
        fabAddNewTicket = findViewById(R.id.fabAddTicket);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventList = new ArrayList<>();
        getDataFromDatabase();

        eventAdapter = new EventAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);

        eventAdapter.setOnItemClickListener(position -> {
            Toast.makeText(MainActivity.this,
                    eventList.get(position).getName(), Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, EventDetailsActivity.class);
            Event event = eventList.get(position);
            intent.putExtra("eventId", event.id);
            startActivity(intent);
        });

        fabAddNewTicket.setOnClickListener(view -> {
//          manageNewEventFunctionality();
            createEventList(); // initialise pre-filled data entries for tickets with its associated events
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
//            String eventType = editTextEventType.getText().toString();
//
//            // Add the new event to the list
//            Event event = new Event(eventName, eventType);
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
//
//    }

    private void getDataFromDatabase() {
        // Retrieve all events asynchronously using LiveData
        EventDao eventDao = MyApp.getAppDatabase().eventDao();
        LiveData<List<Event>> eventsLiveData = eventDao.getAllEvents();
        eventsLiveData.observe(this, events -> {
            // Handle the list of events here
            eventList.clear();
            eventList.addAll(events);
            eventAdapter.notifyDataSetChanged();
        });
    }

    private void createEventList() {
        EventDao eventDao = MyApp.getAppDatabase().eventDao();

        // Create a list of predetermined events here
        List<Event> events = new ArrayList<>();
        events.add(new Event("12345","Swimming Event", "136 Kloske Road, Brisbane City QLD 4000"));
        events.add(new Event("12345","Swimming Event", "136 Kloske Road, Brisbane City QLD 4000"));
        events.add(new Event("12345","Swimming Event", "136 Kloske Road, Brisbane City QLD 4000"));

        // Insert predetermined events into the database
        AsyncTask.execute(() -> {
            for (Event event : events) {
                eventDao.insert(event);
            }
        });
    }
}
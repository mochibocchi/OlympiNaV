package com.example.olympinav;

import static com.example.olympinav.EventDetailsActivity.EXTRA_END_LOCATION;
import static com.example.olympinav.EventDetailsActivity.EXTRA_START_LOCATION;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.olympinav.generators.Generator;
import com.example.olympinav.models.TravelMethod;
import com.example.olympinav.models.TravelType;
import com.example.olympinav.models.Trip;
import com.google.android.material.card.MaterialCardView;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanTripActivity extends BaseActivity {
  private List<Trip> trips = new ArrayList<>();
  private LocalDateTime datetime;
  private TripPlannerRecyclerViewAdapter adapter;
  private EditText startLocationET;
  private EditText endLocationET;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_plan_trip);
    getPrefilledLocations(); // If user opens up from vehicleDetailsActivity, grab the event address from the event item
    setupActivity();
    setupRecyclerView();
    setupTripDetailsFields();
  }

  private void setupTripDetailsFields() {
    startLocationET = findViewById(R.id.startLocationET);
    endLocationET = findViewById(R.id.endLocationET);

    Spinner tripTypeSpinner = findViewById(R.id.tripTypeSpinner);
    ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,
        R.array.plan_trip_options, android.R.layout.simple_spinner_item);
    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    tripTypeSpinner.setAdapter(arrayAdapter);

    EditText tripDateTimeET = findViewById(R.id.tripDateTimeET);
    tripDateTimeET.setOnClickListener(v -> {
      Calendar date;
      final Calendar currentDate = Calendar.getInstance();
      date = Calendar.getInstance();
      new DatePickerDialog(PlanTripActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
        date.set(year, monthOfYear, dayOfMonth);
        new TimePickerDialog(PlanTripActivity.this, (view1, hourOfDay, minute) -> {
          date.set(Calendar.HOUR_OF_DAY, hourOfDay);
          date.set(Calendar.MINUTE, minute);
          datetime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
          tripDateTimeET.setText(datetime.format(DateTimeFormatter.ofPattern("hh:mma dd/MM/yy")));
        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
      }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    });


    tripTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (arrayAdapter.getItem(i).equals("Now")) {
          tripDateTimeET.setText("");
          tripDateTimeET.setVisibility(View.GONE);
          datetime = LocalDateTime.now();
        } else {
          tripDateTimeET.setVisibility(View.VISIBLE);
          tripDateTimeET.callOnClick();
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {
      }
    });

    Button searchButton = findViewById(R.id.searchButton);
    searchButton.setOnClickListener(v -> {
      if (startLocationET.getText().length() == 0) {
        Toast.makeText(PlanTripActivity.this, "Please enter a start location", Toast.LENGTH_SHORT).show();
        return;
      }
      if (endLocationET.getText().length() == 0) {
        Toast.makeText(PlanTripActivity.this, "Please enter an end location", Toast.LENGTH_SHORT).show();
        return;
      }
      trips = new ArrayList<>();
      if (tripTypeSpinner.getSelectedItemPosition() == 2)
        for (int i = 0; i < 4; i++) trips.add(Generator.generateTripBackwards(datetime));
      else
        for (int i = 0; i < 4; i++) trips.add(Generator.generateTripForwards(datetime));
      adapter.setTrips(trips);
      adapter.notifyDataSetChanged();
    });
  }

  private void setupRecyclerView() {
    RecyclerView recyclerView = findViewById(R.id.recyclerView);
    adapter = new TripPlannerRecyclerViewAdapter(trips);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
  }

  class TripPlannerRecyclerViewAdapter extends RecyclerView.Adapter<TripPlannerRowViewHolder> {
    private List<Trip> trips;

    public TripPlannerRecyclerViewAdapter(List<Trip> trips) {
      this.trips = trips;
    }

    @NonNull
    @Override
    public TripPlannerRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(PlanTripActivity.this).inflate(R.layout.plan_trip_row, parent, false);
      return new TripPlannerRowViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TripPlannerRowViewHolder v, int position) {
      Trip trip = trips.get(position);
      Map<TravelType, Integer> counts = new HashMap<>(TravelType.values().length);
      Map<TravelType, Integer> durations = new HashMap<>(TravelType.values().length);
      for (TravelMethod tm : trip.getTravelMethods()) {
        counts.merge(tm.getType(), 1, Integer::sum);
        durations.merge(tm.getType(), (int) ChronoUnit.MINUTES.between(tm.getDepartAt(), tm.getArriveAt()), Integer::sum);
      }
      v.walkCard.setVisibility(!counts.containsKey(TravelType.WALK) ? View.GONE : View.VISIBLE);
      v.busCard.setVisibility(!counts.containsKey(TravelType.BUS) ? View.GONE : View.VISIBLE);
      v.trainCard.setVisibility(!counts.containsKey(TravelType.TRAIN) ? View.GONE : View.VISIBLE);
      v.ferryCard.setVisibility(!counts.containsKey(TravelType.FERRY) ? View.GONE : View.VISIBLE);

      for (Map.Entry<TravelType, Integer> countEntry : counts.entrySet()) {
        if (countEntry.getKey() == TravelType.WALK) {
          v.walkCount.setText(countEntry.getValue() + " walks");
          v.walkDuration.setText(durations.getOrDefault(TravelType.WALK, 0) + " minutes");
        } else if (countEntry.getKey() == TravelType.BUS) {
          v.busCount.setText(countEntry.getValue() + " buses");
          v.busDuration.setText(durations.getOrDefault(TravelType.BUS, 0) + " minutes");
        } else if (countEntry.getKey() == TravelType.TRAIN) {
          v.trainCount.setText(countEntry.getValue() + " trains");
          v.trainDuration.setText(durations.getOrDefault(TravelType.TRAIN, 0) + " minutes");
        } else if (countEntry.getKey() == TravelType.FERRY) {
          v.ferryCount.setText(countEntry.getValue() + " ferrys");
          v.ferryDuration.setText(durations.getOrDefault(TravelType.FERRY, 0) + " minutes");
        }
      }

      LocalDateTime tripStartTime = trip.getTravelMethods().get(0).getDepartAt();
      LocalDateTime tripEndTime = trip.getTravelMethods().get(trip.getTravelMethods().size() - 1).getArriveAt();
      int tripDuration = (int) ChronoUnit.MINUTES.between(tripStartTime, tripEndTime);
      v.leaveAt.setText(tripStartTime.format(DateTimeFormatter.ofPattern("hh:mma")));
      v.arriveAt.setText(tripEndTime.format(DateTimeFormatter.ofPattern("hh:mma")));
      v.duration.setText(tripDuration + " minutes");

      v.clickDetector.setOnClickListener(view -> {
        Intent i = new Intent(PlanTripActivity.this, ViewTripActivity.class);
        i.putExtra("trip", trip);
        startActivity(i);
      });
    }


    @Override
    public int getItemCount() {
      return trips.size();
    }

    public void setTrips(List<Trip> trips) {
      this.trips = trips;
    }
  }

  private class TripPlannerRowViewHolder extends RecyclerView.ViewHolder {
    private MaterialCardView walkCard;
    private TextView walkCount;
    private TextView walkDuration;

    private MaterialCardView busCard;
    private TextView busCount;
    private TextView busDuration;

    private MaterialCardView trainCard;
    private TextView trainCount;
    private TextView trainDuration;

    private MaterialCardView ferryCard;
    private TextView ferryCount;
    private TextView ferryDuration;

    private TextView leaveAt;
    private TextView duration;
    private TextView arriveAt;
    private ConstraintLayout clickDetector;

    public TripPlannerRowViewHolder(@NonNull View itemView) {
      super(itemView);
      walkCard = itemView.findViewById(R.id.walkCard);
      walkCount = itemView.findViewById(R.id.walkCount);
      walkDuration = itemView.findViewById(R.id.walkDuration);

      busCard = itemView.findViewById(R.id.busCard);
      busCount = itemView.findViewById(R.id.busCount);
      busDuration = itemView.findViewById(R.id.busDuration);

      trainCard = itemView.findViewById(R.id.trainCard);
      trainCount = itemView.findViewById(R.id.trainCount);
      trainDuration = itemView.findViewById(R.id.trainDuration);

      ferryCard = itemView.findViewById(R.id.ferryCard);
      ferryCount = itemView.findViewById(R.id.ferryCount);
      ferryDuration = itemView.findViewById(R.id.ferryDuration);

      leaveAt = itemView.findViewById(R.id.leaveAt);
      duration = itemView.findViewById(R.id.totalDuration);
      arriveAt = itemView.findViewById(R.id.arriveAt);

      clickDetector = itemView.findViewById(R.id.clickDetector);
    }
  }

  private void getPrefilledLocations() {
    Intent intent = getIntent();
    if (intent.hasExtra(EXTRA_START_LOCATION) && intent.hasExtra(EXTRA_END_LOCATION)) {
      String startLocation = intent.getStringExtra(EXTRA_START_LOCATION);
      String endLocation = intent.getStringExtra(EXTRA_END_LOCATION);

      EditText tvStartLocationET = findViewById(R.id.startLocationET);
      tvStartLocationET.setText(startLocation);

      EditText tvEndLocationET = findViewById(R.id.endLocationET);
      tvEndLocationET.setText(endLocation);
    }
  }
}

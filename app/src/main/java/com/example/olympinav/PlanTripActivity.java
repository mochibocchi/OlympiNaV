package com.example.olympinav;

import static com.example.olympinav.EventDetailsActivity.EXTRA_END_LOCATION;
import static com.example.olympinav.EventDetailsActivity.EXTRA_START_LOCATION;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.olympinav.Utils.MyApp;
import com.example.olympinav.Utils.Utils;
import com.example.olympinav.generators.Generator;
import com.example.olympinav.models.TravelMethod;
import com.example.olympinav.models.TravelType;
import com.example.olympinav.models.Trip;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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
    setupActivity("Plan Trip");
    setupRecyclerView();
    setupTripDetailsFields();
    setupNoiseSensitivityToggle(); // If user toggles Sensitive to Noise on, it will add more to the noise level.
    setupPrioritiseSeatsToggle(); // If user toggles Prioritise Seats on, it will add more to the capacity level.
  }
  private void setupPrioritiseSeatsToggle() {
    Switch prioritiseSeatsToggle = findViewById(R.id.PrioritiseSeatsToggle);
    prioritiseSeatsToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
    updateTripPrioritiseSeatsState(isChecked);
    });
  }
  private void updateTripPrioritiseSeatsState(boolean isChecked) {
    adapter.setPrioritiseSeatsActive(isChecked);
    adapter.setTrips(trips);
    adapter.notifyDataSetChanged();
  }
  private void setupNoiseSensitivityToggle() {
    Switch sensitiveToNoiseToggle = findViewById(R.id.SensitiveToNoiseToggle);
    sensitiveToNoiseToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
    updateTripSensitivityState(isChecked);
    });
  }
  private void updateTripSensitivityState(boolean isChecked) {
    adapter.setSensitiveToNoiseActive(isChecked);
    adapter.setTrips(trips);
    adapter.notifyDataSetChanged();
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
      int options = ThreadLocalRandom.current().nextInt(3, 6);
      if (tripTypeSpinner.getSelectedItemPosition() == 2)
        for (int i = 0; i < options; i++) trips.add(Generator.generateTripBackwards(datetime));
      else
        for (int i = 0; i < options; i++) trips.add(Generator.generateTripForwards(datetime));
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
    private boolean isSensitiveToNoiseActive;
    private boolean isPrioritiseSeatsActive;

    public TripPlannerRecyclerViewAdapter(List<Trip> trips) {
      this.trips = trips;
      this.isSensitiveToNoiseActive = false;
      this.isPrioritiseSeatsActive = false;
    }
    public void setSensitiveToNoiseActive(boolean active) {
      isSensitiveToNoiseActive = active;
    }

    public void setPrioritiseSeatsActive(boolean active) {
      isPrioritiseSeatsActive = active;
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

      Map<TravelType, Double> averageNoiseLevelByType = trip.getTravelMethods().stream()
          .collect(Collectors.groupingBy(TravelMethod::getType,
              Collectors.averagingInt(tm -> tm.getNoiseLevel().toProgressBarPercentage())));
      Map<TravelType, Double> averageUsedCapacityByType = trip.getTravelMethods().stream()
          .collect(Collectors.groupingBy(TravelMethod::getType,
              Collectors.averagingInt(tm -> tm.getUsedCapacity().toProgressBarPercentage())));

      v.walkRow.setVisibility(!counts.containsKey(TravelType.WALK) ? View.GONE : View.VISIBLE);
      v.busRow.setVisibility(!counts.containsKey(TravelType.BUS) ? View.GONE : View.VISIBLE);
      v.trainRow.setVisibility(!counts.containsKey(TravelType.TRAIN) ? View.GONE : View.VISIBLE);
      v.ferryRow.setVisibility(!counts.containsKey(TravelType.FERRY) ? View.GONE : View.VISIBLE);

      for (Map.Entry<TravelType, Integer> countEntry : counts.entrySet()) {
        String count = String.valueOf(countEntry.getValue());
        String name = Utils.calculateWordForQuantity(countEntry.getKey().toString().toLowerCase(), countEntry.getValue());
        String duration = durations.getOrDefault(countEntry.getKey(), 0) + " minutes";

        int noiseLevel = (int) Math.round(averageNoiseLevelByType.getOrDefault(countEntry.getKey(), 0d));
        int usedCapacity = (int) Math.round(averageUsedCapacityByType.getOrDefault(countEntry.getKey(), 0d));

        if (isSensitiveToNoiseActive) {
          noiseLevel = Math.max(0,
              noiseLevel + SensorData.NoiseBaseLevelThreshold.getValue() + MyApp.getUser().getUser().getNoiseBaselineLevel());
        }

        if (isPrioritiseSeatsActive) {
          usedCapacity = Math.max(0, usedCapacity + SensorData.PrioritiseSeatsThreshold.getValue());
        }

        @ColorRes int noiseLevelColor = Utils.getProgressBarColor(noiseLevel);
        @ColorRes int usedCapacityColor = Utils.getProgressBarColor(usedCapacity);

        if (countEntry.getKey() == TravelType.WALK) {
          v.walkCount.setText(count);
          v.walkName.setText(name);
          v.walkDuration.setText(duration);
          v.walkNoiseLevel.setProgress(noiseLevel);
          v.walkNoiseLevel.setProgressTintList(ColorStateList.valueOf(getResources().getColor(noiseLevelColor)));
          v.walkUsedCapcity.setProgress(usedCapacity);
          v.walkUsedCapcity.setProgressTintList(ColorStateList.valueOf(getResources().getColor(usedCapacityColor)));
        } else if (countEntry.getKey() == TravelType.BUS) {
          v.busCount.setText(count);
          v.busName.setText(name);
          v.busDuration.setText(duration);
          v.busNoiseLevel.setProgress(noiseLevel);
          v.busNoiseLevel.setProgressTintList(ColorStateList.valueOf(getResources().getColor(noiseLevelColor)));
          v.busUsedCapcity.setProgress(usedCapacity);
          v.busUsedCapcity.setProgressTintList(ColorStateList.valueOf(getResources().getColor(usedCapacityColor)));
        } else if (countEntry.getKey() == TravelType.TRAIN) {
          v.trainCount.setText(count);
          v.trainName.setText(name);
          v.trainDuration.setText(duration);
          v.trainNoiseLevel.setProgress(noiseLevel);
          v.trainNoiseLevel.setProgressTintList(ColorStateList.valueOf(getResources().getColor(noiseLevelColor)));
          v.trainUsedCapcity.setProgress(usedCapacity);
          v.trainUsedCapcity.setProgressTintList(ColorStateList.valueOf(getResources().getColor(usedCapacityColor)));
        } else if (countEntry.getKey() == TravelType.FERRY) {
          v.ferryCount.setText(count);
          v.ferryName.setText(name);
          v.ferryDuration.setText(duration);
          v.ferryNoiseLevel.setProgress(noiseLevel);
          v.ferryNoiseLevel.setProgressTintList(ColorStateList.valueOf(getResources().getColor(noiseLevelColor)));
          v.ferryUsedCapcity.setProgress(usedCapacity);
          v.ferryUsedCapcity.setProgressTintList(ColorStateList.valueOf(getResources().getColor(usedCapacityColor)));
        }
      }

      LocalDateTime tripStartTime = trip.getTravelMethods().get(0).getDepartAt();
      LocalDateTime tripEndTime = trip.getTravelMethods().get(trip.getTravelMethods().size() - 1).getArriveAt();
      int tripDuration = (int) ChronoUnit.MINUTES.between(tripStartTime, tripEndTime);
      v.leaveAt.setText("Leave At\n" + tripStartTime.format(DateTimeFormatter.ofPattern("hh:mma")));
      v.arriveAt.setText("Arrive At\n" + tripEndTime.format(DateTimeFormatter.ofPattern("hh:mma")));
      v.duration.setText("Duration\n" + tripDuration + " minutes");

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
    private TableRow walkRow;
    private TextView walkCount;
    private TextView walkName;
    private TextView walkDuration;
    private ProgressBar walkNoiseLevel;
    private ProgressBar walkUsedCapcity;

    private TableRow busRow;
    private TextView busCount;
    private TextView busName;
    private TextView busDuration;
    private ProgressBar busNoiseLevel;
    private ProgressBar busUsedCapcity;

    private TableRow trainRow;
    private TextView trainCount;
    private TextView trainName;
    private TextView trainDuration;
    private ProgressBar trainNoiseLevel;
    private ProgressBar trainUsedCapcity;

    private TableRow ferryRow;
    private TextView ferryCount;
    private TextView ferryName;
    private TextView ferryDuration;
    private ProgressBar ferryNoiseLevel;
    private ProgressBar ferryUsedCapcity;

    private TextView leaveAt;
    private TextView duration;
    private TextView arriveAt;
    private ConstraintLayout clickDetector;

    public TripPlannerRowViewHolder(@NonNull View itemView) {
      super(itemView);
      walkRow = itemView.findViewById(R.id.walkRow);
      walkCount = itemView.findViewById(R.id.walkCount);
      walkName = itemView.findViewById(R.id.walkName);
      walkDuration = itemView.findViewById(R.id.walkDuration);
      walkNoiseLevel = itemView.findViewById(R.id.walkNoiseLevelProgressBar);
      walkUsedCapcity = itemView.findViewById(R.id.walkUsedCapacityProgressBar);

      busRow = itemView.findViewById(R.id.busRow);
      busCount = itemView.findViewById(R.id.busCount);
      busName = itemView.findViewById(R.id.busName);
      busDuration = itemView.findViewById(R.id.busDuration);
      busNoiseLevel = itemView.findViewById(R.id.busNoiseLevelProgressBar);
      busUsedCapcity = itemView.findViewById(R.id.busUsedCapacityProgressBar);

      trainRow = itemView.findViewById(R.id.trainRow);
      trainCount = itemView.findViewById(R.id.trainCount);
      trainName = itemView.findViewById(R.id.trainName);
      trainDuration = itemView.findViewById(R.id.trainDuration);
      trainNoiseLevel = itemView.findViewById(R.id.trainNoiseLevelProgressBar);
      trainUsedCapcity = itemView.findViewById(R.id.trainUsedCapacityProgressBar);

      ferryRow = itemView.findViewById(R.id.ferryRow);
      ferryCount = itemView.findViewById(R.id.ferryCount);
      ferryName = itemView.findViewById(R.id.ferryName);
      ferryDuration = itemView.findViewById(R.id.ferryDuration);
      ferryNoiseLevel = itemView.findViewById(R.id.ferryNoiseLevelProgressBar);
      ferryUsedCapcity = itemView.findViewById(R.id.ferryUsedCapacityProgressBar);

      leaveAt = itemView.findViewById(R.id.leaveAt);
      duration = itemView.findViewById(R.id.duration);
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

package com.example.olympinav;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.olympinav.models.TransportationMethod;
import com.example.olympinav.models.TransportationMethodType;
import com.example.olympinav.models.Trip;
import com.google.android.material.card.MaterialCardView;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanTripActivity extends BaseActivity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_plan_trip);
    setupActivity();

    Spinner tripTypeSpinner = findViewById(R.id.tripTypeSpinner);
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        R.array.plan_trip_options, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    tripTypeSpinner.setAdapter(adapter);

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
          LocalDateTime datetime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
          tripDateTimeET.setText(datetime.format(DateTimeFormatter.ofPattern("hh:mma dd/MM/yy")));
        }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
      }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    });


    tripTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapter.getItem(i).equals("Now")) {
          tripDateTimeET.setText("");
          tripDateTimeET.setVisibility(View.GONE);
        } else {
          tripDateTimeET.setVisibility(View.VISIBLE);
          tripDateTimeET.callOnClick();
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {
      }
    });
  }

  private class TripPlannerRecyclerView extends RecyclerView.Adapter<TripPlannerRowViewHolder> {
    private List<Trip> trips;

    @NonNull
    @Override
    public TripPlannerRowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View v = LayoutInflater.from(PlanTripActivity.this).inflate(R.layout.plan_trip_row, parent, false);
      return new TripPlannerRowViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TripPlannerRowViewHolder v, int position) {
      Trip trip = trips.get(v.getAdapterPosition());
      Map<TransportationMethodType, Integer> counts = new HashMap<>(4);
      Map<TransportationMethodType, Integer> durations = new HashMap<>(4);
      for (TransportationMethod tm : trip.getiDontKnow()) {
        counts.merge(tm.getType(), 1, Integer::sum);
        durations.merge(tm.getType(), (int) ChronoUnit.MINUTES.between(tm.getBoardAt(), tm.getDepartAt()), Integer::sum);
      }
      if (!counts.containsKey(TransportationMethodType.WALK))
        v.walkCard.setVisibility(View.GONE);
      if (!counts.containsKey(TransportationMethodType.BUS))
        v.walkCard.setVisibility(View.GONE);
      if (!counts.containsKey(TransportationMethodType.TRAIN))
        v.walkCard.setVisibility(View.GONE);
      if (!counts.containsKey(TransportationMethodType.FERRY))
        v.walkCard.setVisibility(View.GONE);

      for (Map.Entry<TransportationMethodType, Integer> countEntry : counts.entrySet()) {
        if (countEntry.getKey() == TransportationMethodType.WALK) {
          v.walkCount.setText(countEntry.getValue() + " walks");
          v.walkDuration.setText(countEntry.getValue() + " minutes");
        } else if (countEntry.getKey() == TransportationMethodType.BUS) {
          v.busCount.setText(countEntry.getValue() + " buses");
          v.busDuration.setText(countEntry.getValue() + " minutes");
        } else if (countEntry.getKey() == TransportationMethodType.TRAIN) {
          v.trainCount.setText(countEntry.getValue() + " trains");
          v.trainDuration.setText(countEntry.getValue() + " minutes");
        } else if (countEntry.getKey() == TransportationMethodType.FERRY) {
          v.ferryCount.setText(countEntry.getValue() + " ferrys");
          v.ferryDuration.setText(countEntry.getValue() + " minutes");
        }
      }

      LocalDateTime tripStartTime = trip.getiDontKnow().get(0).getBoardAt();
      LocalDateTime tripEndTime = trip.getiDontKnow().get(trip.getiDontKnow().size() - 1).getDepartAt();
      int tripDuration = (int) ChronoUnit.MINUTES.between(tripStartTime, tripEndTime);
      v.leaveAt.setText(tripStartTime.format(DateTimeFormatter.ofPattern("hh:mma")));
      v.arriveAt.setText(tripStartTime.format(DateTimeFormatter.ofPattern("hh:mma")));
      v.duration.setText(tripDuration + " minutes");
    }


    @Override
    public int getItemCount() {
      return trips.size();
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
      duration = itemView.findViewById(R.id.duration);
      arriveAt = itemView.findViewById(R.id.arriveAt);
    }
  }
}

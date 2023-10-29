package com.example.olympinav;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.olympinav.models.TravelMethod;
import com.example.olympinav.models.TravelType;
import com.example.olympinav.models.Trip;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ViewTripActivity extends BaseActivity {
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mma");
  private Trip trip;
  private TripStintReyclerView adapter;
  private GoogleMap map;
  private AlertDialog dialog;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_view_trip);
    SupportMapFragment mapFragment = (SupportMapFragment)  getSupportFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(m -> {
        map = m;
        map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(-27.4705, 153.0260), 12,
            30, 0)));
    });
    setupActivity();
    trip = (Trip) getIntent().getParcelableExtra("trip");
    setupViews();
    setupRecyclerView();
  }

  private void setupViews() {
      TextView time = findViewById(R.id.time);
      time.setText(trip.getDepartAt().format(timeFormatter) + " - " + trip.getArriveAt().format(timeFormatter));
      TextView duration = findViewById(R.id.duration);
      duration.setText(trip.getDuration(ChronoUnit.MINUTES) + " minutes");
      TextView price = findViewById(R.id.price);
      price.setText("$3.51"); // Don't have a price system
      // Finish button opens up a feedback dialogue:
      FloatingActionButton finishBtn = findViewById(R.id.openFeedbackDialogueFAB);
      finishBtn.setOnClickListener(v -> {
          PromptUserForTripFeedback();
      });
  }

  private void setupRecyclerView() {
    RecyclerView recyclerView = findViewById(R.id.recyclerView);
    adapter = new TripStintReyclerView(trip.getTravelMethods());
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.setAdapter(adapter);
  }

  class TripStintReyclerView extends RecyclerView.Adapter<TripStintViewHolder> {
      List<TravelMethod> travelMethods;

      public TripStintReyclerView(List<TravelMethod> tms) {
          this.travelMethods = tms;
      }

    @NonNull
    @Override
    public TripStintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ViewTripActivity.this).inflate(R.layout.row_trip_stint, parent, false);
        return new TripStintViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TripStintViewHolder v, int position) {
        TravelMethod tm = travelMethods.get(position);

        // Hide all bottom dots except for the last one.
        if (position != getItemCount() - 1) {
            v.bottomDot.setVisibility(View.GONE);
            v.departLocation.setVisibility(View.GONE);
        }

        @ColorRes int color = tm.getType().getColor();
        @DrawableRes int drawable = tm.getType().getDrawable();
        v.topDot.setCardBackgroundColor(getResources().getColor(color));
        v.section.setCardBackgroundColor(getResources().getColor(color));
        v.bottomDot.setCardBackgroundColor(getResources().getColor(color));
        v.transportNumberParent.setStrokeColor(getResources().getColor(color));
        v.transportNumber.setTextColor(getResources().getColor(color));
        v.busImage.setBackgroundResource(drawable);
        v.boardLocation.setTextColor(getResources().getColor(color));
        v.departLocation.setTextColor(getResources().getColor(color));
        v.tripDuration.setTextColor(getResources().getColor(color));
        v.noiseLevel.setTextColor(getResources().getColor(color));
        v.busyness.setTextColor(getResources().getColor(color));

        v.boardLocation.setText("Board Location");
        v.departLocation.setText("Depart Location");
        v.transportNumber.setText(tm.getType() == TravelType.WALK ? "WALK" : tm.getRouteNumber());
        v.tripDuration.setText(ChronoUnit.MINUTES.between(tm.getDepartAt(), tm.getArriveAt()) + " minutes");

        if (tm.getType() != TravelType.WALK) {
            v.noiseLevel.setText(tm.getNoiseLevel().getDisplayString());
            v.busyness.setText(tm.getUsedCapacity().getDisplayString());
        } else {
            v.noiseLevel.setVisibility(View.GONE);
            v.busyness.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
      return travelMethods.size();
    }
  }

  static class TripStintViewHolder extends RecyclerView.ViewHolder {
      CardView topDot;
      CardView section;
      CardView bottomDot;

      MaterialCardView transportNumberParent;
      ImageView busImage;
      TextView transportNumber;

      TextView boardLocation;
      TextView departLocation;

      TextView tripDuration;
      TextView noiseLevel;
      TextView busyness;

      public TripStintViewHolder(View itemView) {
          super(itemView);

          topDot = itemView.findViewById(R.id.topDot);
          section = itemView.findViewById(R.id.section);
          bottomDot = itemView.findViewById(R.id.bottomDot);
          transportNumberParent = itemView.findViewById(R.id.transportNumberParent);
          busImage = itemView.findViewById(R.id.busImage);
          transportNumber = itemView.findViewById(R.id.transportNumber);
          boardLocation = itemView.findViewById(R.id.boardLocation);
          departLocation = itemView.findViewById(R.id.departLocation);
          tripDuration = itemView.findViewById(R.id.tripDuration);
          noiseLevel = itemView.findViewById(R.id.noiseLevel);
          busyness = itemView.findViewById(R.id.busyness);
      }
  }

//    This is our IoT solution for transforming noise level sensor data:
//    - There will be sound sensors on public transports, monitoring noise levels real-time.
//    - Each user has their own personalised, stored baseline audio level threshold preference.
//    - Each user will be able to personalise their threshold by feeding it more data through feedback.
//    - That is, at the end of every trip, the user is prompted "How was the sound level?"
//      then they will select sad face, neutral face, happy face.
//
//    - Additionally, we have a 'global baseline' sound level threshold which is fed through the backend api - as in, this is
//      what the average people think this is the acceptable noise.
//
//    This global baseline will also continue to change based on feedback from the users when they finish a trip.
//
//    Finally, we transform the data and putting it into real-life use by comparing the global baseline
//    versus the user's personalised sound level baseline threshold.
//    This real-life application will be useful for people with ADHD or autism, and for those who may
//    are uncomfortable with loud environments.

    private void PromptUserForTripFeedback() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_end_of_trip_feedback, null);
        builder.setView(dialogView);

        ImageButton happyButton = dialogView.findViewById(R.id.noiseLevelHappyFace);
        ImageButton neutralButton = dialogView.findViewById(R.id.noiseLevelNeutralFace);
        ImageButton sadButton = dialogView.findViewById(R.id.noiseLevelSadFace);

        happyButton.setOnClickListener(v -> {
            Toast.makeText(this, "Thanks for your feedback!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            Intent intent = new Intent(ViewTripActivity.this, MainActivity.class);
            startActivity(intent);
        });

        neutralButton.setOnClickListener(v -> {
            updateUserNoiseBaseLevel(10);
            Toast.makeText(this, "We'll adjust your personalised noise threshold.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            Intent intent = new Intent(ViewTripActivity.this, MainActivity.class);
            startActivity(intent);
        });

        sadButton.setOnClickListener(v -> {
            updateUserNoiseBaseLevel(25);
            Toast.makeText(this, "We'll adjust your personalised noise threshold.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            Intent intent = new Intent(ViewTripActivity.this, MainActivity.class);
            startActivity(intent);
        });

        dialog = builder.create();
        builder.setNegativeButton("Cancel", null);
        dialog.show();
    }

    private void updateUserNoiseBaseLevel(int feedback) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDataPref", Context.MODE_PRIVATE);
        int currentBaseLevel = sharedPreferences.getInt("UserNoiseBaselineLevel", 0);

        currentBaseLevel += feedback;

        // Save the updated user's updated UserNoiseBaseLevel back to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("UserNoiseBaseLevel", currentBaseLevel);
        editor.apply();
    }
}

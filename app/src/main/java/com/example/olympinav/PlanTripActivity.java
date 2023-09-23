package com.example.olympinav;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

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

        View.OnClickListener l = v -> startActivity(new Intent(PlanTripActivity.this, ViewTripActivity.class));

        findViewById(R.id.card1).setOnClickListener(l);
        findViewById(R.id.card2).setOnClickListener(l);

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
}

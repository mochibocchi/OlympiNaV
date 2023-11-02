package com.example.olympinav;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.olympinav.Utils.MyApp;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MyApp.getUser() == null)
            logout();
    }

    protected void setupActivity(String pageTitle) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(pageTitle);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        String[] languages = {"English", "German", "French", "Chinese", "Japanese"};
        Integer[] flags = {R.drawable.united_states_flag, R.drawable.german_flag, R.drawable.french_flag,
            R.drawable.chinese_flag, R.drawable.japanese_flag};

        Spinner spinner = findViewById(R.id.languageSpinner);
        spinner.setAdapter(new ImageTextSpinnerAdapter(this, R.layout.image_text_spinner_value_layout, languages, flags));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        // Here, you can add more items in the navigation bar on the left.
        if (itemId == R.id.nav_home) {
            // To navigate to a new activity, change the activity here:
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.nav_plan_trip) {
            Intent intent = new Intent(this, PlanTripActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.nav_service_updates) {
            Intent intent = new Intent(this, ServiceUpdatesActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.nav_feedback) {
            Intent intent = new Intent(this, FeedbackActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.nav_logout) {
            logout();
        } else if (itemId == R.id.nav_resetusersettings) {
            resetUserNoiseBaseLevel();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void resetUserNoiseBaseLevel() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDataPref", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("UserNoiseBaseLevel", 0);
        editor.apply();

        Toast.makeText(this, "Resetting user's personalised settings threshold", Toast.LENGTH_SHORT).show();
    }

    private void logout() {
        MyApp.setUser(null);
        startActivity(new Intent(BaseActivity.this, LoginActivity.class));
    }
}

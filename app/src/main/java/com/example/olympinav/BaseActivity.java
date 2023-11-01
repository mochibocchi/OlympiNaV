package com.example.olympinav;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupActivity(String pageTitle) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(pageTitle);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        } else if (itemId == R.id.nav_language) {
            Intent intent = new Intent(this, LanguageActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.nav_logout) {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
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
}

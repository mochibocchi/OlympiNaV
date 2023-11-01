package com.example.olympinav;

import android.content.SharedPreferences;

public class UserSettingsManager {
    private final SharedPreferences sharedPreferences;

    public UserSettingsManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void updateUserNoiseBaseLevel(int feedback) {
        int currentBaseLevel = sharedPreferences.getInt("UserNoiseBaselineLevel", 0);
        currentBaseLevel += feedback;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("UserNoiseBaseLevel", currentBaseLevel);
        editor.apply();
    }
}

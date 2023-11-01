package com.example.olympinav.Utils;

import android.app.Application;

import androidx.room.Room;

import com.example.olympinav.DB.AppDatabase;

public class MyApp extends Application {
    private static AppDatabase appDatabase;
    private static String DATABASE_NAME = "OLYMPINAV_DATABASE";

    @Override
    public void onCreate() {
        super.onCreate();
        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
    }

    public static AppDatabase getAppDatabase() {
        return appDatabase;
    }
}



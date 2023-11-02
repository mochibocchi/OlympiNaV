package com.example.olympinav.Utils;

import android.app.Application;

import androidx.room.Room;

import com.example.olympinav.DB.AppDatabase;
import com.example.olympinav.DB.UserWithTicketsAndEvents;

public class MyApp extends Application {
    private static AppDatabase appDatabase;
    private static UserWithTicketsAndEvents user;
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

    public static void setUser(UserWithTicketsAndEvents user) {
        MyApp.user = user;
    }

    public static UserWithTicketsAndEvents getUser() {
        return user;
    }
}



package com.example.cliff.fitnessapp;

import android.app.Application;

public class FitnessApplication
        extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidDatabase.initializeDatabase(new FitnessAppHelper(getApplicationContext()));
    }
}

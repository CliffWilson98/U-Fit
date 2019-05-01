package com.example.cliff.fitnessapp;

import android.database.Cursor;

public class WorkoutQueryResult implements QueryResult {
    private Cursor cursor;

    public WorkoutQueryResult(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public int getInt(String column) {
        return 0;
    }

    @Override
    public String getString(String column) {
        return null;
    }

    @Override
    public void moveToNext() {

    }

    @Override
    public void moveToFirst() {

    }
}

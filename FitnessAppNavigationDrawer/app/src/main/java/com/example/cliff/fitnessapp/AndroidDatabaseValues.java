package com.example.cliff.fitnessapp;

import android.content.ContentValues;

public class AndroidDatabaseValues implements DatabaseValues {
    private ContentValues contentValues;

    public static ContentValues toContentValues(DatabaseValues databaseValues) {
        if(databaseValues instanceof AndroidDatabaseValues) {
            return ((AndroidDatabaseValues) databaseValues).contentValues;
        } else {
            throw new UnsupportedOperationException("Cannot convert non-android database values to ContentValues");
        }
    }

    @Override
    public void clear() {
        contentValues.clear();
    }

    public AndroidDatabaseValues() {
        contentValues = new ContentValues();
    }

    public void put(String key, int value) {
        contentValues.put(key, value);
    }

    public void put(String key, String value) {
        contentValues.put(key, value);
    }
}

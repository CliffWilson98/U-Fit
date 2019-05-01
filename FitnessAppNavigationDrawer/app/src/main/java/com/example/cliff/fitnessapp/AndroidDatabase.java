package com.example.cliff.fitnessapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class WorkoutDatabase extends Database {
    private SQLiteDatabase sqliteDatabase = null;

    private WorkoutDatabase(Context context) {
        FitnessAppHelper helper = new FitnessAppHelper(context);
    }

    public static void initializeDatabase(Context context) {
        theDatabase = new WorkoutDatabase(context);
    }

    @Override
    public QueryResult query(String query) {
        return new WorkoutQueryResult(theDatabase.rawQuery(query, null));
    }
}
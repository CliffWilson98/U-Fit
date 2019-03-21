package com.example.cliff.fitnessapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FitnessAppHelper extends SQLiteOpenHelper
{
    private static final String DB_NAME = "Fitness_App";
    private static final int DB_VERSION = 1;

    FitnessAppHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db)
    {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //If the old version is less than 1 then that means the database has not been created
        //so the tables must be made for the first time
        if (oldVersion < 1)
        {
            //TODO maybe split each table creation into own method?
            db.execSQL("CREATE TABLE EXERCISE (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "NAME TEXT," +
                    "REPS INTEGER," +
                    "SETS INTEGER," +
                    "WEIGHT INTEGER," +
                    "USESWEIGHT INTEGER," +
                    "WORKOUT INTEGER);");

            db.execSQL("CREATE TABLE WORKOUT (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "NAME TEXT);");

            db.execSQL("CREATE TABLE PROFILE (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "NAME TEXT," +
                    "PICTURE BLOB," +
                    "AGE INTEGER," +
                    "GENDER TEXT," +
                    "HEIGHT TEXT," +
                    "WEIGHT TEXT," +
                    "NECK TEXT," +
                    "CHEST TEXT," +
                    "WAIST TEXT," +
                    "HIPS TEXT);");

            //this table is for holding the results of any exercise when it is completed so
            //it can be displayed on the stats screen.
            //For example, if a user completes a 5x5 bench press at 200 lbs then the weight, sets,
            //and reps will be saved.
            db.execSQL("CREATE TABLE EXERCISERESULTS(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "NAME TEXT," +
                    "WEIGHT INTEGER," +
                    "REPS INTEGER," +
                    "SETS INTEGER," +
                    "EXERCISERESULTSTABLEID INTEGER);");

            db.execSQL("CREATE TABLE EXERCISERESULTSTABLE(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "NAME TEXT);");

            ContentValues profileValues = new ContentValues();
            profileValues.put("NAME", "New User");
            profileValues.put("AGE", 23);
            profileValues.put("GENDER", "Male");
            profileValues.put("HEIGHT", "0.0");
            profileValues.put("WEIGHT", "0.0");
            profileValues.put("NECK", "0.0");
            profileValues.put("CHEST", "0.0");
            profileValues.put("WAIST", "0.0");
            profileValues.put("HIPS", "0.0");

            db.insert("PROFILE", null, profileValues);

            ContentValues exerciseNames = new ContentValues();
            exerciseNames.put("NAME", "Deadlift");
            db.insert("EXERCISERESULTSTABLE", null, exerciseNames);
            exerciseNames.clear();
            exerciseNames.put("NAME", "Bench Press");
            db.insert("EXERCISERESULTSTABLE", null, exerciseNames);
            exerciseNames.clear();
            exerciseNames.put("NAME", "Squat");
            db.insert("EXERCISERESULTSTABLE", null, exerciseNames);
            exerciseNames.clear();
            exerciseNames.put("NAME", "Overhead Press");
            db.insert("EXERCISERESULTSTABLE", null, exerciseNames);

            //TODO remove this
            //putting dummy data just to make sure that the database is working

            /*ContentValues fakeExerciseResults = new ContentValues();
            fakeExerciseResults.put("NAME", "Deadlift");
            fakeExerciseResults.put("WEIGHT", 120);
            fakeExerciseResults.put("REPS", 5);
            fakeExerciseResults.put("SETS", 5);
            fakeExerciseResults.put("EXERCISERESULTSTABLEID", 1);
            db.insert("EXERCISERESULTS", null, fakeExerciseResults);

            fakeExerciseResults.clear();
            fakeExerciseResults.put("NAME", "Deadlift");
            fakeExerciseResults.put("WEIGHT", 175);
            fakeExerciseResults.put("REPS", 5);
            fakeExerciseResults.put("SETS", 5);
            fakeExerciseResults.put("EXERCISERESULTSTABLEID", 1);
            db.insert("EXERCISERESULTS", null, fakeExerciseResults);

            fakeExerciseResults.put("NAME", "Deadlift");
            fakeExerciseResults.put("WEIGHT", 210);
            fakeExerciseResults.put("REPS", 5);
            fakeExerciseResults.put("SETS", 5);
            fakeExerciseResults.put("EXERCISERESULTSTABLEID", 1);
            db.insert("EXERCISERESULTS", null, fakeExerciseResults);*/

        }
        if (oldVersion < 2)
        {
            //For future use
        }

    }

    //helper method to add rows to the exercise table
    private static void insertExercise(SQLiteDatabase db, String name, int reps, int numberOfReps, int weight, int usesWeight)
    {
        ContentValues exerciseValues = new ContentValues();
        exerciseValues.put("NAME", name);
        exerciseValues.put("REPS", reps);
        exerciseValues.put("NUMBEROFREPS", numberOfReps);
        exerciseValues.put("WEIGHT", weight);
        exerciseValues.put("USESWEIGHT", usesWeight);
    }

    //helper method to add rows to the workout table
    private static void insertWorkout()
    {
    }
}

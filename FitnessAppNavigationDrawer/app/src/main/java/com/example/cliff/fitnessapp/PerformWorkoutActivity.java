package com.example.cliff.fitnessapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.icu.text.SymbolTable;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;

import java.util.ArrayList;

public class PerformWorkoutActivity extends AppCompatActivity {

    private String workoutName;
    private ArrayList<Exercise> exerciseList = new ArrayList<>();
    private boolean[] wasExerciseSkippedArray;
    private int currentExerciseIndex;

    private boolean isResting;

    private boolean isWorkoutFinished;

    private int repCounter;
    private int setCounter;
    private String counterButtonText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        currentExerciseIndex = 0;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform_workout);

        isResting = false;
        isWorkoutFinished = false;

        Toolbar toolbar = (Toolbar) findViewById(R.id.workout_toolbar);
        setSupportActionBar(toolbar);

        getWorkoutDetails();
        updateTextViews();

        runTimer();

        //initially the user has done zero reps and is on the first set
        resetRepsAndSets();
    }

    private void runTimer()
    {
        final TextView timerTextView = findViewById(R.id.timer_text_view);
        final Handler handler = new Handler();
        timerTextView.setTextColor(Color.BLACK);
        handler.post(new Runnable() {
            int seconds = 0;

            @Override
            public void run() {
                seconds++;
                System.out.println("timer is running");
                String timerText = String.format("Timer %02d:%02d", seconds/60, seconds % 60);
                timerTextView.setText(timerText);
                if (!isWorkoutFinished)
                {
                    handler.postDelayed(this, 1000);
                }
            }

        });
    }

    private void resetRepsAndSets()
    {
        repCounter = 0;
        setCounter = 0;
    }

    //this will get the workouts name and populate the exercise ArrayList
    private void getWorkoutDetails()
    {
        int workoutID = getIntent().getIntExtra("workoutID", 0);

        FitnessAppHelper helper = new FitnessAppHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        //getting the workout name
        Cursor cursor = db.rawQuery("SELECT * FROM WORKOUT WHERE _id = " + workoutID, null);
        cursor.moveToFirst();
        workoutName = cursor.getString(cursor.getColumnIndex("NAME"));

        //populating the exercise ArrayList
        cursor = db.rawQuery("SELECT * FROM EXERCISE WHERE WORKOUT = " + workoutID, null);
        cursor.moveToFirst();
        do
        {
            String exerciseName = cursor.getString(cursor.getColumnIndex("NAME"));
            int reps = cursor.getInt(cursor.getColumnIndex("REPS"));
            int sets = cursor.getInt(cursor.getColumnIndex("SETS"));
            int weight = cursor.getInt(cursor.getColumnIndex("WEIGHT"));

            Exercise exercise = new Exercise(exerciseName, reps, sets, weight);
            exerciseList.add(exercise);
        }while (cursor.moveToNext());

       setSizeOfWasExerciseSkippedArray();
    }

    private void setSizeOfWasExerciseSkippedArray()
    {
        wasExerciseSkippedArray = new boolean[exerciseList.size()];
    }

    private void updateTextViews()
    {
        TextView nameTextView = findViewById(R.id.workout_name);
        nameTextView.setTextColor(Color.rgb(156, 160, 163));
        nameTextView.setText(workoutName);

        Exercise currentExercise = exerciseList.get(currentExerciseIndex);

        TextView exerciseTextView = findViewById(R.id.exercise_instructions);
        exerciseTextView.setTextColor(Color.WHITE);
        String exerciseInstructions = String.format("%dx%d %s at %d lbs", currentExercise.getSets(), currentExercise.getReps(), currentExercise.getName(), currentExercise.getWeight());
        exerciseTextView.setText(exerciseInstructions);
    }

    public void incrementCounter(View v)
    {
        if (!isResting)
        {
            Exercise currentExercise = exerciseList.get(currentExerciseIndex);
            Button button = (Button) findViewById(R.id.counter_button);

            if (isLastSetOfExercise(currentExercise))
            {
                resetRepsAndSets();
                markCurrentExerciseAsCompleted();
                goToNextExerciseOrGoToMainActivity();
            }
            else
            {
                setCounter++;
                repCounter = currentExercise.getReps();
            }

            String buttonText = String.format("Finished set %d\n with %d reps!", setCounter, repCounter);
            setCounterButtonText(buttonText);
            button.setTextSize(14);
            button.setTextColor(Color.WHITE);
            button.setText(buttonText);
        }

    }

    private boolean isLastSetOfExercise(Exercise currentExercise)
    {
        if (setCounter == (currentExercise.getSets() - 1))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    private String setCounterButtonText(String buttonText) {
        return this.counterButtonText = buttonText;
    }

    private void markCurrentExerciseAsCompleted()
    {
        wasExerciseSkippedArray[currentExerciseIndex] = false;
    }

    public void goToNextExercise()
    {
        currentExerciseIndex++;
        updateTextViews();
    }

    private void goToNextExerciseOrGoToMainActivity ()
    {
        if (isNotLastExercise())
        {
            goToNextExercise();
        }
        else
        {
            isWorkoutFinished = true;
            updateDatabaseWithCompletedExercises(getDatabase());
            returnToMainActivity();
        }
    }

    private SQLiteDatabase getDatabase()
    {
        FitnessAppHelper helper = new FitnessAppHelper(this);
        return helper.getReadableDatabase();
    }

    private void updateDatabaseWithCompletedExercises(SQLiteDatabase db)
    {
        getCompletedExercises();
        addCompletedExercisesToDatabase(getCompletedExercises(), db);
    }

    private ArrayList<Exercise> getCompletedExercises()
    {
        ArrayList<Exercise> completedExerciseList = new ArrayList<>();

        for (int i = 0; i < wasExerciseSkippedArray.length; i++)
        {
            if (exerciseWasCompleted(i))
            {
                completedExerciseList.add(exerciseList.get(i));
            }
        }

        return completedExerciseList;
    }

    private void addCompletedExercisesToDatabase(ArrayList<Exercise> exercisesToAdd, SQLiteDatabase db)
    {
        ContentValues exerciseResults = new ContentValues();

        for (Exercise e : exercisesToAdd)
        {
            int id = findCorrespondingIdOfExercise(e, db);

            exerciseResults.put("NAME", e.getName());
            exerciseResults.put("WEIGHT", e.getWeight());
            exerciseResults.put("REPS", e.getReps());
            exerciseResults.put("SETS", e.getSets());
            exerciseResults.put("DEFINEDEXERCISEID", id);

            db.insert("EXERCISERESULTS", null, exerciseResults);
            exerciseResults.clear();
        }
    }

    private int findCorrespondingIdOfExercise(Exercise e, SQLiteDatabase db)
    {
        Cursor cursor = db.rawQuery("SELECT * FROM DEFINEDEXERCISE WHERE NAME = ? ", new String[]{e.getName()});
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    private boolean exerciseWasCompleted(int exerciseIndex)
    {
        return !wasExerciseSkippedArray[exerciseIndex];
    }

    private boolean isNotLastExercise() {
        return (currentExerciseIndex < (exerciseList.size() - 1));
    }

    private void returnToMainActivity()
    {
        //TODO remove this
        String testString = "";
        for (boolean exerciseBoolean : wasExerciseSkippedArray)
        {
            testString += exerciseBoolean;
            testString += " ";
        }
        System.out.println("EXERCISE IS FINISHED!");
        System.out.println(testString);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void skipExercise(View v)
    {
        resetRepsAndSets();
        markCurrentExerciseAsSkipped();
        goToNextExerciseOrGoToMainActivity();
    }

    private void markCurrentExerciseAsSkipped()
    {
        wasExerciseSkippedArray[currentExerciseIndex] = true;
    }

    //The back button needs to be disabled in this activity
    //If the user wants to quit a workout they will use the cancel workout button
    @Override
    public void onBackPressed()
    {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Press cancel workout if you want to go back!",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    //confirms whether or not the user wants to cancel the workout when they press the cancel button
    public void confirmCancelWorkout(View v)
    {
        AlertDialog diaBox = askOption();
        diaBox.show();
    }

    private void cancelWorkout()
    {
        //for now when the workout is cancelled it will just take you back to the main activity
        isWorkoutFinished = true;
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Used to create dialog box to confirm that the user wants to cancel a workout
    private AlertDialog askOption()
    {
        AlertDialog deleteDialogBox = new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Cancel Workout")
                .setMessage("Are you sure you want to cancel this workout?")
                //TODO change this icon, it is just to test
                .setIcon(R.drawable.ic_menu_send)

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        cancelWorkout();
                        dialog.dismiss();
                    }

                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        return deleteDialogBox;
    }

    public void restTimer(View v)
    {
        if (!isResting)
        {
            setCounterTextResting();
            Button restButton = (Button) v;
            int restTime = Integer.parseInt(restButton.getText().toString());
            restTimerHandler(restTime);
            System.out.println("Clicked button: " + restTime);
        }
        else
        {
            createAlreadyRestingToast();
        }
    }

    private void createAlreadyRestingToast()
    {
        Toast.makeText(getApplicationContext(),"You are already resting!",Toast.LENGTH_SHORT).show();
    }

    private void restTimerHandler(final int restTime) {

        final Button counterButton = findViewById(R.id.counter_button);
        final Handler handler = new Handler();
        counterButton.setTextSize(20);
        counterButton.setTextColor(Color.RED);
        handler.post(new Runnable() {

            final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            int timer = restTime;

            @Override
            public void run() {
                timer--;
                System.out.println("Timer: " + timer);
                counterButton.setText(String.format("Time: %d", timer));
                if (timer > 0 && !isWorkoutFinished && isResting) {
                    handler.postDelayed(this, 1000);
                }
                else
                {
                    v.vibrate(500);
                    setCounterTextNotResting();
                }
            }

        });
    }

    private void setCounterTextResting()
    {
        isResting = true;
        Button b = findViewById(R.id.counter_button);
        b.setText("Resting");
    }

    private void setCounterTextNotResting()
    {
        if (isResting == true)
        {
            isResting = false;
        }
        Button b = findViewById(R.id.counter_button);
        b.setTextSize(14);
        b.setTextColor(Color.WHITE);
        if (counterButtonText != null)
        {
            b.setText(counterButtonText);
        }
        else
        {
            b.setText("Counter");
        }
    }

    public void cancelRest(View v)
    {
        isResting = false;
    }

}

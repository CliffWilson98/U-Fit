package com.example.cliff.fitnessapp;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateWorkoutFragment extends Fragment implements View.OnClickListener {


    private ArrayList<Exercise> exerciseList;
    private String workoutNames = "";
    private ArrayList<String> exerciseListView;

    //Required empty public constructor
    public CreateWorkoutFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        exerciseList = new ArrayList<>();
        exerciseListView = new ArrayList<>();

        CreateWorkoutAdapter createWorkoutAdapter = new CreateWorkoutAdapter(exerciseListView, this.getContext());
        View v = inflater.inflate(R.layout.fragment_create_workout, container, false);

        ListView listView = (ListView) v.findViewById(R.id.workout_list_view);
        listView.setAdapter(createWorkoutAdapter);

        Button addExerciseButton = (Button) v.findViewById(R.id.add_exercise_button);
        addExerciseButton.setOnClickListener(this);
        addExerciseButton.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER) {

                }
                return false;
            }
        });

        Button createWorkoutButton = (Button) v.findViewById(R.id.create_workout_button);
        createWorkoutButton.setOnClickListener(this);

        return v;
    }

    @Override
    public void onStart()
    {
        Spinner spinner = (Spinner) getView().findViewById(R.id.create_workout_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.workouts_array, R.layout.create_workout_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        super.onStart();
    }

    //TODO make it where two workouts with the same name cannot be created.
    public void onClick(View v)
    {
        int id = v.getId();

        if (id == R.id.add_exercise_button)
        {
            boolean repTextIsEmpty = ((EditText)(getView().findViewById(R.id.rep_edit_text))).getText().toString().equals("");
            boolean setsTextIsEmpty = ((EditText)(getView().findViewById(R.id.sets_edit_text))).getText().toString().equals("");
            boolean weightTextIsEmpty = ((EditText)(getView().findViewById(R.id.weight_edit_text))).getText().toString().equals("");

            //The exercise will not be created if there are not values in all of the required text fields
            if (!(repTextIsEmpty) && !(setsTextIsEmpty) && !(weightTextIsEmpty))
            {
                createExercise();
            }

        }
        else if (id == R.id.create_workout_button)
        {
            EditText workoutNameTextView = getView().findViewById(R.id.workout_name);
            String workoutName = workoutNameTextView.getText().toString();

            //the workout will not be created unless the workout has a name
            //and there are exercises in the list
            if(!(workoutName.equals("")) && exerciseList.size() != 0)
            {
                System.out.println("Creating workout");
                createWorkout(workoutName);
            }
        }
    }

    private void createExercise()
    {
        //Get data from necessary fields and add it to the exercise ArrayList
        String exerciseName = (String)(((Spinner)getView().findViewById(R.id.create_workout_spinner)).getSelectedItem().toString());
        int reps = Integer.valueOf((((EditText)getView().findViewById(R.id.rep_edit_text)).getText().toString()));
        int sets = Integer.valueOf((((EditText)getView().findViewById(R.id.sets_edit_text)).getText().toString()));
        int weight = Integer.valueOf((((EditText)getView().findViewById(R.id.weight_edit_text)).getText().toString()));

        Exercise exerciseToAdd = new Exercise(exerciseName, reps, sets, weight);
        exerciseList.add(exerciseToAdd);

        exerciseListView.add(exerciseToAdd.getName());


        //displayAddedExercises();
    }

    private void createWorkout(String workoutName)
    {
        SQLiteOpenHelper helper = new FitnessAppHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put("NAME", workoutName);

        db.insert("WORKOUT", null, value);

        Cursor cursor = db.rawQuery("SELECT * FROM WORKOUT ORDER BY _id DESC LIMIT 1;", null);
        cursor.moveToFirst();

        int workoutID = cursor.getInt(0);

        addExercisesToDatabase(workoutID);
        exerciseList.clear();

        Toast toast = Toast.makeText(getActivity(), String.format("%s: %s", "Workout created", workoutName), Toast.LENGTH_LONG);
        toast.show();

    }

    private void addExercisesToDatabase(int workoutId)
    {
        SQLiteOpenHelper helper = new FitnessAppHelper(getActivity());

        for (int i = 0; i < exerciseList.size(); i ++)
        {
            Exercise exercise = exerciseList.get(i);

            String name = exercise.getName();
            int reps = exercise.getReps();
            int sets = exercise.getSets();
            int weight = exercise.getWeight();

            ContentValues value = new ContentValues();
            value.put("NAME", name);
            value.put("REPS", reps);
            value.put("SETS", sets);
            value.put("WEIGHT", weight);
            value.put("WORKOUT", workoutId);

            try
            {
                SQLiteDatabase db = helper.getWritableDatabase();
                db.insert("EXERCISE", null, value);
            }
            catch(SQLiteException e)
            {
                Toast toast = Toast.makeText(getActivity(), "Database Unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }


    private void displayAddedExercises()
    {
        // create a StringBuilder for writing exercises to the ArrayList
        StringBuilder sb = new StringBuilder();
        // create an ArrayList so that added exercises have an index to reference each exercise
        ArrayList<String> exercises = new ArrayList<>();
        String exercise;

        // loops through all exercises in the list and add each one to the ArrayList
        for (int i = 0; i < exerciseList.size(); i++) {
            exercise = String.format("%s%d: %s \n%s: %d   %s: %d  %s: %d\n",
                    "exercise #", i+1, exerciseList.get(i).getName(),
                    "reps", exerciseList.get(i).getReps(),
                    "sets", exerciseList.get(i).getSets(),
                    "weight", exerciseList.get(i).getWeight());

            exercises.add(exercise);
        }
        /**
        // create a TextView to display to the user
        TextView addedExercises = getView().findViewById(R.id.exercise_database_contents);

        // loops through each new exercise that was created by the user
        for (String element : exercises) {
            sb.append(element);
        }

        // adds those exercises to the TextView
        addedExercises.setText(sb.toString());
         **/
    }

}

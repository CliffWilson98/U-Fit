package com.example.cliff.fitnessapp;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateWorkoutFragment extends Fragment implements View.OnClickListener {

    //Handle the exercises for each workout
    private ArrayList<Exercise> exerciseList;
    //Handle the TextView within the ListView exercises for the adapter
    private ArrayList<String> exerciseListView;
    //Handle the TextView to populate the ListView via the adapter
    private ListView listView;
    private CreateWorkoutAdapter createWorkoutAdapter;

    //Required empty public constructor
    public CreateWorkoutFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        exerciseList = new ArrayList<>();
        exerciseListView = new ArrayList<>();

        createWorkoutAdapter = new CreateWorkoutAdapter(exerciseListView, this.getActivity(), this);
        View v = inflater.inflate(R.layout.fragment_create_workout, container, false);

        listView = (ListView) v.findViewById(R.id.workout_list_view);
        listView.setAdapter(createWorkoutAdapter);

        Button addExerciseButton = (Button) v.findViewById(R.id.add_exercise_button);
        addExerciseButton.setOnClickListener(this);

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
        createWorkoutAdapter.notifyDataSetChanged();

        //whenever an exercise is added the listview size must be changed
        justifyListViewHeightBasedOnChildren();
    }

    public void removeExercise (int position) {
        //Remove the exercise from the ListView for the adapter
        exerciseListView.remove(position);

        //Remove the exercise from the ArrayList of exercises for the database
        exerciseList.remove(position);

        //Resize the ListView to match the number of exercises
        justifyListViewHeightBasedOnChildren();
    }

    private void justifyListViewHeightBasedOnChildren () {
        if (createWorkoutAdapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < createWorkoutAdapter.getCount(); i++) {
            View listItem = createWorkoutAdapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (createWorkoutAdapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
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

        //TESTING to see what exercises are actually added
        for (int i = 0; i < exerciseList.size(); i++) {
            System.out.println("exercise list: " + exerciseList.get(i).getName());
        }

        //Clear list of exercises
        exerciseList.clear();
        //Clear ListView of exercises
        exerciseListView.clear();
        //Notify adapter that ListView has changed
        createWorkoutAdapter.notifyDataSetChanged();
        //Resize the ListView to match the number of exercises
        justifyListViewHeightBasedOnChildren();

        // verify to the user that a workout was created
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

}

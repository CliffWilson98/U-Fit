package com.example.cliff.fitnessapp;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyWorkoutsFragment extends Fragment {

    private ListView workoutList;
    private ArrayList<String> workoutNameList = new ArrayList<>();
    private ArrayList<Integer> workoutIDList = new ArrayList<>();

    public MyWorkoutsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_my_workouts, container, false);

        workoutList = (ListView)(v.findViewById(R.id.workout_listview));

        workoutList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String workoutName = (String) parent.getItemAtPosition(position);
                int workoutID = workoutIDList.get(position);

                System.out.println("Item " + workoutName + "ID " + workoutID);

                Intent intent = new Intent(getActivity(), WorkoutDetailActivity.class);
                intent.putExtra("WorkoutID", workoutID);
                intent.putExtra("WorkoutName", workoutName);
                startActivity(intent);
            }
        });

        populateListView();

        // Inflate the layout for this fragment
        return v;
    }

    @Override
    public void onStart()
    {
        displayWorkouts();
        super.onStart();
    }

    private void populateListView()
    {
        FitnessAppHelper helper = new FitnessAppHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM WORKOUT", null);
        cursor.moveToFirst();

        do
        {
            int workoutID = cursor.getInt(0);
            workoutIDList.add(workoutID);
            String workoutName = cursor.getString(1);
            workoutNameList.add(workoutName);
        }while (cursor.moveToNext());

        workoutList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, workoutNameList));
    }

    //TODO remove this
    //this method is just a way to make sure the database is working properly
    private void displayWorkouts()
    {
        String text = "";

        SQLiteOpenHelper helper = new FitnessAppHelper(getActivity());

        try
        {
            SQLiteDatabase db = helper.getReadableDatabase();

            Cursor workoutCursor = db.rawQuery("SELECT * FROM WORKOUT ORDER BY _id", null);
            workoutCursor.moveToFirst();

            do
            {
                int workoutID = workoutCursor.getInt(0);

                text += workoutCursor.getString(workoutCursor.getColumnIndex("NAME"));

                Cursor exerciseCursor = db.rawQuery("SELECT * FROM EXERCISE WHERE WORKOUT = " + workoutID + "", null);
                exerciseCursor.moveToFirst();

                do
                {
                    text += " " + exerciseCursor.getString(exerciseCursor.getColumnIndex("NAME"));
                }
                while(exerciseCursor.moveToNext());

                text += "\n";
            }
            while (workoutCursor.moveToNext());
        }
        catch(SQLiteException e)
        {
            Toast.makeText(getActivity(), "DATABASE UNAVAILABLE", Toast.LENGTH_SHORT).show();
        }

        TextView workoutText = (TextView)(getView().findViewById(R.id.my_workouts_text));
        //System.out.println("WORKOUT " + workoutText);
        workoutText.setText(text);
    }

}

package com.example.cliff.fitnessapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyWorkoutsFragment extends Fragment {

    private ListView workoutList;
    private ArrayList<String> workoutNameList = new ArrayList<>();
    private ArrayList<Integer> workoutIDList = new ArrayList<>();
    private WhiteTextAdapter whiteTextAdapter;

    public MyWorkoutsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_my_workouts, container, false);

        whiteTextAdapter = new WhiteTextAdapter(getActivity(), R.layout.white_text_listview, workoutNameList);
        workoutList = (ListView)(v.findViewById(R.id.workout_listview));
        workoutList.setAdapter(whiteTextAdapter);

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
        //displayWorkouts();
        super.onStart();
    }

    private void populateListView()
    {
        Database db = Database.getDatabase();
        QueryResult queryResult = db.query("SELECT * FROM WORKOUT");
        queryResult.moveToFirst();

        int workoutCount = queryResult.getCount();

        System.out.println("NUMBER OF WORKOUTS: " + workoutCount);

        if (queryResult.getCount() != 0)
        {
            do
            {
                int workoutID = queryResult.getInt(0);
                workoutIDList.add(workoutID);
                String workoutName = queryResult.getString(1);
                workoutNameList.add(workoutName);
            }while (queryResult.moveToNext());
        }

    }

    //TODO remove this
    //this method is just a way to make sure the database is working properly
    private void displayWorkouts() {
        String text = "";

        Database db = Database.getDatabase();

        QueryResult workoutQueryResult = db.query("SELECT * FROM WORKOUT ORDER BY _id");
        workoutQueryResult.moveToFirst();

        //TODO delete this!
        QueryResult testQueryResult = db.query("SELECT * FROM EXERCISE");
        testQueryResult.moveToFirst();
        System.out.println("Total exercises " + testQueryResult.getCount());

        do {
            int workoutID = workoutQueryResult.getInt(0);

            text += workoutQueryResult.getString("NAME");

            QueryResult exerciseQueryResult = db.query("SELECT * FROM EXERCISE WHERE WORKOUT = " + workoutID);
            exerciseQueryResult.moveToFirst();

            System.out.println("Exercise Count: " + exerciseQueryResult.getCount());

            do {
                text += " " + exerciseQueryResult.getString("NAME");
            }
            while (exerciseQueryResult.moveToNext());

            text += "\n";
        }
        while (workoutQueryResult.moveToNext());
    }

}

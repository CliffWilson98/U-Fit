package com.example.cliff.fitnessapp;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */

//TODO scale Y axis on every graph
public class StatsFragment extends Fragment {

    private ArrayList<Graph> graphs = new ArrayList<>();

    private Spinner exerciseNamesSpinner;

    public StatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stats, container, false);
        exerciseNamesSpinner = v.findViewById(R.id.stats_screen_exercise_spinner);

        setUpExerciseNamesSpinnerAdapter();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        initializeGraphs();
    }

    private void setUpExerciseNamesSpinnerAdapter()
    {
        populateSpinnerWithExerciseNames();
        addListenerToSpinner();
    }

    private void populateSpinnerWithExerciseNames()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                getArrayListOfExerciseNames()
        );

        exerciseNamesSpinner.setAdapter(adapter);
    }

    private ArrayList<String> getArrayListOfExerciseNames()
    {
        FitnessAppHelper helper = new FitnessAppHelper(getActivity());
        return helper.getDefinedExerciseNames();
    }

    private void addListenerToSpinner()
    {
        exerciseNamesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int databaseId = convertSpinnerPositionToDatabaseId(position);
                retrieveDatabaseInformationForExerciseAndUpdateGraph(databaseId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

            private int convertSpinnerPositionToDatabaseId(int position)
            {
                return position + 1;
            }


        });
    }

    private GraphView getGraphViewById(int id) {
        return (GraphView)getView().findViewById(id);
    }

    private void initializeGraphs()
    {
        Graph weightGraph = new Graph(getGraphViewById(R.id.weight_graph), "WEIGHT");
        Graph repGraph = new Graph(getGraphViewById(R.id.rep_graph), "REPS");
        Graph setGraph = new Graph(getGraphViewById(R.id.set_graph), "SETS");

        graphs.add(weightGraph);
        graphs.add(repGraph);
        graphs.add(setGraph);
    }

    private void retrieveDatabaseInformationForExerciseAndUpdateGraph(int exerciseID)
    {
        clearValuesFromInformationArrays();
        populateExerciseInformationArrays(getCursorFromExerciseId(getDatabase(), exerciseID));
        updateEveryGraph();
    }

    private void updateEveryGraph()
    {
        for (Graph graph : graphs) {
            graph.updateGraph();
        }
    }

    private SQLiteDatabase getDatabase()
    {
        FitnessAppHelper helper = new FitnessAppHelper(getActivity());
        return helper.getReadableDatabase();
    }

    private Cursor getCursorFromExerciseId(SQLiteDatabase db, int id)
    {
        return db.rawQuery("SELECT * FROM EXERCISERESULTS WHERE DEFINEDEXERCISEID = " + id,null);
    }

    private void populateExerciseInformationArrays(Cursor cursor)
    {
        if (cursor.getCount() != 0)
        {
            cursor.moveToFirst();
            do {
                for(Graph graph : graphs) {
                    graph.addDataFromCursor(cursor);
                }
            }while(cursor.moveToNext());
        }
    }

    private void clearValuesFromInformationArrays()
    {
        for(Graph graph : graphs) {
            graph.clear();
        }
    }

}

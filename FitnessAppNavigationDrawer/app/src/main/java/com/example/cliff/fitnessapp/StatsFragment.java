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
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */

//TODO scale Y axis on every graph
public class StatsFragment extends Fragment {

    private ArrayList<Integer> weightList = new ArrayList<Integer>();
    private ArrayList<Integer> setList = new ArrayList<Integer>();
    private ArrayList<Integer> repList = new ArrayList<Integer>();

    private GraphView weightGraph;
    private GraphView repGraph;
    private GraphView setGraph;

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
                retrieveDatabaseInformationForExerciseAndUpdateGraph(convertSpinnerPositionToDatabaseId(position));
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

    private void initializeGraphs()
    {
        weightGraph = (GraphView) getView().findViewById(R.id.weight_graph);
        weightGraph.getViewport().setXAxisBoundsManual(true);
        weightGraph.getViewport().setYAxisBoundsManual(true);

        setGraph = (GraphView) getView().findViewById(R.id.set_graph);
        setGraph.getViewport().setXAxisBoundsManual(true);
        setGraph.getViewport().setYAxisBoundsManual(true);

        repGraph = (GraphView) getView().findViewById(R.id.rep_graph);
        repGraph.getViewport().setXAxisBoundsManual(true);
        repGraph.getViewport().setYAxisBoundsManual(true);
    }

    private void retrieveDatabaseInformationForExerciseAndUpdateGraph(int exerciseID)
    {
        clearValuesFromInformationArrays();
        populateExerciseInformationArrays(getCursorFromExerciseId(getDatabase(), exerciseID));
        updateEveryGraph();
    }

    private void updateEveryGraph()
    {
        updateGraph(weightGraph, weightList);
        updateGraph(repGraph, repList);
        updateGraph(setGraph, setList);
    }


    private void updateGraph(GraphView graph, ArrayList<Integer> list)
    {
        graph.removeAllSeries();
        DataPoint[] pointArray = generateDataPointArrayFromArrayList(list);
        graph.addSeries(createLineGraphSeriesFromPointArray(pointArray));
        setGraphXAxisBounds(graph, list);
        setGraphYAxisBounds(graph, list);
    }

    private void setGraphXAxisBounds(GraphView graph, ArrayList<Integer> list)
    {
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(list.size() - 1);
    }

    private void setGraphYAxisBounds(GraphView graph, ArrayList<Integer> list)
    {
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(getMaxValueFromList(list));
    }

    private int getMaxValueFromList(ArrayList<Integer> list)
    {
        if (!list.isEmpty())
        {
            return Collections.max(list);
        }
        else
        {
            return 0;
        }
    }

    private DataPoint[] generateDataPointArrayFromArrayList(ArrayList<Integer> list)
    {
            DataPoint[] pointArray = new DataPoint[list.size()];

            for (int i = 0; i < pointArray.length; i ++)
            {
                pointArray[i] = new DataPoint(i, list.get(i));
            }

            return pointArray;
    }

    private LineGraphSeries<DataPoint> createLineGraphSeriesFromPointArray(DataPoint[] pointArray)
    {
        return new LineGraphSeries<>(pointArray);
    }

    private SQLiteDatabase getDatabase()
    {
        FitnessAppHelper helper = new FitnessAppHelper(getActivity());
        return helper.getReadableDatabase();
    }

    //For now this method does not need to be used.
    /*private int getIdOfExerciseFromName(SQLiteDatabase db, String exerciseName)
    {
        Cursor cursor = db.rawQuery("SELECT * FROM DEFINEDEXERCISE WHERE NAME = ?", new String[] {exerciseName});
        cursor.moveToFirst();
        return cursor.getInt(0);
    }*/

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
                weightList.add(getWeightFromCursor(cursor));
                setList.add(getSetsFromCursor(cursor));
                repList.add(getRepsFromCursor(cursor));
            }while(cursor.moveToNext());
        }
    }

    private void clearValuesFromInformationArrays()
    {
        weightList.clear();
        setList.clear();
        repList.clear();
    }

    private int getWeightFromCursor(Cursor cursor)
    {
        return cursor.getInt(cursor.getColumnIndex("WEIGHT"));
    }

    private int getSetsFromCursor(Cursor cursor)
    {
        return cursor.getInt(cursor.getColumnIndex("SETS"));
    }

    private int getRepsFromCursor(Cursor cursor)
    {
        return cursor.getInt(cursor.getColumnIndex("REPS"));
    }

    private String getNameFromCursor(Cursor cursor)
    {
        return cursor.getString(cursor.getColumnIndex("NAME"));
    }


}

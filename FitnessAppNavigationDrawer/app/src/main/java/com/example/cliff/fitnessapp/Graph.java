package com.example.cliff.fitnessapp;

import android.database.Cursor;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Collections;

public class Graph {
    private ArrayList<Integer> dataPoints = new ArrayList<>();
    private String databaseColumn;
    private GraphView view;

    public Graph(GraphView graphView, String databaseColumn) {
        this.view = graphView;
        view.getViewport().setXAxisBoundsManual(true);
        view.getViewport().setYAxisBoundsManual(true);

        this.databaseColumn = databaseColumn;
    }

    public void clear() {
        dataPoints.clear();
    }

    public void updateGraph() {
        updateSeries();
        updateBounds();
    }

    private void updateSeries() {
        view.removeAllSeries();
        DataPoint[] pointArray = generateDataPointArrayFromArrayList(dataPoints);
        view.addSeries(createLineGraphSeriesFromPointArray(pointArray));
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

    private void updateBounds()
    {
        view.getViewport().setMinX(0);
        view.getViewport().setMaxX(dataPoints.size() - 1);
        view.getViewport().setMinY(0);
        view.getViewport().setMaxY(getMaxValueFromList(dataPoints));
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

    public void addDataFromQueryResult(QueryResult queryResult) {
        int data = queryResult.getInt(databaseColumn);
        dataPoints.add(data);
    }
}

package com.example.cliff.fitnessapp;

public class Exercise
{
    private String name;
    private int reps;
    private int numberOfReps;

    //default constructor
    public Exercise(){};

    public Exercise(String name, int reps, int numberOfReps)
    {
        this.name = name;
        this.reps = reps;
        this.numberOfReps = numberOfReps;
    }

    public String getName()
    {
        return name;
    }

    public int getReps()
    {
        return reps;
    }

    public int getNumberOfReps()
    {
        return numberOfReps;
    }
}

package com.example.cliff.fitnessapp;

public class Exercise
{
    private String name;
    private int reps;
    private int sets;
    private int weight;

    //default constructor
    public Exercise(){};

    public Exercise(String name, int reps, int numberOfReps, int weight)
    {
        this.name = name;
        this.reps = reps;
        this.sets = numberOfReps;
        this.weight = weight;
    }

    public String getName()
    {
        return name;
    }

    public int getReps()
    {
        return reps;
    }

    public int getSets()
    {
        return sets;
    }

    public int getWeight(){return weight; };
}

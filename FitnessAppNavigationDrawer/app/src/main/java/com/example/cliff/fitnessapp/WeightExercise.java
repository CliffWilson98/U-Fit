package com.example.cliff.fitnessapp;

public class WeightExercise extends Exercise
{
    private int weight;

    public WeightExercise(String name, int reps, int numberOfReps, int weight)
    {
        super(name, reps, numberOfReps);
        this.weight = weight;
    }

    public int getWeight()
    {
        return weight;
    }
}

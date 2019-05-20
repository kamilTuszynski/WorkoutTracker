package com.workouttracker.models;

public class SetParameters {

    //TODO: uwzglednic czy dane cwiczenie pozwala i na pdostawie tego to tworzyc. jak?
    private int weight;
    private int reps;
    private float rpe;

    public SetParameters() {
    }

    public SetParameters(int weight, int reps, float rpe) {
        this.weight = weight;
        this.reps = reps;
        this.rpe = rpe;
    }

    public int getWeight() {
        return weight;
    }

    public int getReps() {
        return reps;
    }

    public float getRpe() {
        return rpe;
    }
}

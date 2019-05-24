package com.workouttracker.models;

public class TrainingSet {
    private String exerciseName;
    private int reps;
    private float rpe;

    public TrainingSet() {
    }

    public TrainingSet(String exerciseName, int reps, float rpe) {
        this.exerciseName = exerciseName;
        this.reps = reps;
        this.rpe = rpe;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public int getReps() {
        return reps;
    }

    public float getRpe() {
        return rpe;
    }
}

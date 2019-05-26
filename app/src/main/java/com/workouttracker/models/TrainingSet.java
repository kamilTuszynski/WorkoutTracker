package com.workouttracker.models;

public class TrainingSet {
    private String exerciseName;
    private int reps;
    private float rpe;
    private long timeMillis;

    public TrainingSet() {
    }

    public TrainingSet(String exerciseName, int reps, float rpe, long timeMillis) {
        this.exerciseName = exerciseName;
        this.reps = reps;
        this.rpe = rpe;
        this.timeMillis = timeMillis;
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

    public long getTimeMillis() {
        return timeMillis;
    }
}

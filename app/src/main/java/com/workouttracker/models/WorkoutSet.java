package com.workouttracker.models;

public class WorkoutSet {
    private String exerciseName;
    private float weight;
    private int reps;
    private float rpe;
    private long timeMillis;


    public WorkoutSet() {
    }

    public WorkoutSet(String exerciseName, float weight, int reps, float rpe, long timeMillis) {
        this.exerciseName = exerciseName;
        this.weight = weight;
        this.reps = reps;
        this.rpe = rpe;
        this.timeMillis = timeMillis;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public float getWeight() {
        return weight;
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

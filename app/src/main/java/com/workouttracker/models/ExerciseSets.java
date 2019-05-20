package com.workouttracker.models;

import java.util.List;

public class ExerciseSets {
    private Exercise exercise;
    List<SetParameters> sets;

    public ExerciseSets(Exercise exercise, List<SetParameters> sets) {
        this.exercise = exercise;
        this.sets = sets;
    }

    public ExerciseSets() {
    }

    public Exercise getExercise() {
        return exercise;
    }

    public List<SetParameters> getSets() {
        return sets;
    }
}

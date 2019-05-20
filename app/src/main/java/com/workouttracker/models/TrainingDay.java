package com.workouttracker.models;

import java.util.List;

public class TrainingDay {
    private int dayNumber;
    private List<ExerciseSets> exercises;

    public TrainingDay(int dayNumber, List<ExerciseSets> exercises) {
        this.dayNumber = dayNumber;
        this.exercises = exercises;
    }

    public TrainingDay() {
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public List<ExerciseSets> getExercises() {
        return exercises;
    }
}

package com.workouttracker.models;

public class TrainingDay {
    private int dayNumber;
    boolean restDay;
    //private List<ExerciseSets> exercises;

    public TrainingDay(int dayNumber, boolean restDay/*, List<ExerciseSets> exercises*/) {
        this.dayNumber = dayNumber;
        this.restDay = restDay;
        //this.exercises = exercises;
    }

    public TrainingDay() {
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public boolean isRestDay() {
        return restDay;
    }
//    public List<ExerciseSets> getExercises() {
//        return exercises;
//    }
}

package com.workouttracker.models;

public class TrainingWeek {
    private int weekNumber;
    //List<TrainingDay> trainingDays;

    public TrainingWeek() {
    }

    public TrainingWeek(int weekNumber/*, List<TrainingDay> trainingDays*/) {
        this.weekNumber = weekNumber;
        //this.trainingDays = trainingDays;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

//    public List<TrainingDay> getTrainingDays() {
//        return trainingDays;
//    }
}

package com.workouttracker.models;

import java.util.List;

public class TrainingPlan {
    private String name;
    private String description;
    private String type;
    private String difficulty;

    private int durationInWeeks;
    List<TrainingWeek> trainingWeeks;

    public TrainingPlan() {
    }

    public TrainingPlan(String name, String description, String type, String difficulty, int durationInWeeks, List<TrainingWeek> trainingWeeks) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.difficulty = difficulty;
        this.durationInWeeks = durationInWeeks;
        this.trainingWeeks = trainingWeeks;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getDurationInWeeks() {
        return durationInWeeks;
    }

    public List<TrainingWeek> getTrainingWeeks() {
        return trainingWeeks;
    }
}

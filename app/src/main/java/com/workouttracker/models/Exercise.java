package com.workouttracker.models;

public class Exercise {
    private String name;
    private boolean repsPossible;
    private boolean weightPossible;
    private boolean timePossible;
    private boolean distancePossible;

    public Exercise(){}

    public Exercise(String name, boolean repsPossible, boolean weightPossible, boolean timePossible, boolean distancePossible) {
        this.name = name;
        this.repsPossible = repsPossible;
        this.weightPossible = weightPossible;
        this.timePossible = timePossible;
        this.distancePossible = distancePossible;
    }

    public String getName() {
        return name;
    }

    public boolean isRepsPossible() {
        return repsPossible;
    }

    public boolean isWeightPossible() {
        return weightPossible;
    }

    public boolean isTimePossible() {
        return timePossible;
    }

    public boolean isDistancePossible() {
        return distancePossible;
    }
}

package com.workouttracker.models;

import java.util.Date;

public class Workout {
    private Date date;
    //private Location location;

    public Workout() {
    }

    public Workout(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }
}

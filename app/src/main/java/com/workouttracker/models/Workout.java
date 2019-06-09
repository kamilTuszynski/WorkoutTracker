package com.workouttracker.models;

import java.util.Date;

public class Workout {
    private Date date;
    private Double placeLatitude;
    private Double placeLongitude;
    private String placeName;

    public Workout() {
    }

    public Workout(Date date, Double placeLatitude, Double placeLongitude, String placeName) {
        this.date = date;
        this.placeLatitude = placeLatitude;
        this.placeLongitude = placeLongitude;
        this.placeName = placeName;
    }

    public Date getDate() {
        return date;
    }

    public double getPlaceLatitude() {
        return placeLatitude;
    }

    public double getPlaceLongitude() {
        return placeLongitude;
    }

    public String getPlaceName() {
        return placeName;
    }
}

package com.workouttracker.models;

import java.util.Date;

public class Workout {
    private Date date;
    private Double placeLatitude;
    private Double placeLongitude;
    private String placeName;
    private String placeAddress;

    public Workout() {
    }

    public Workout(Date date, Double placeLatitude, Double placeLongitude, String placeName, String placeAddress) {
        this.date = date;
        this.placeLatitude = placeLatitude;
        this.placeLongitude = placeLongitude;
        this.placeName = placeName;
        this.placeAddress = placeAddress;
    }


    public Date getDate() {
        return date;
    }

    public Double getPlaceLatitude() {
        return placeLatitude;
    }

    public Double getPlaceLongitude() {
        return placeLongitude;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }
}

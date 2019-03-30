package com.workouttracker.models;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Workout {

    private Map<Exercise, List<SetParameters>> _exercises;
    private Date _date;
}

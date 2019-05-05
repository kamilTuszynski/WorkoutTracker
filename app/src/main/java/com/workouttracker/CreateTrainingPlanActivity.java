package com.workouttracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Spinner;

public class CreateTrainingPlanActivity extends AppCompatActivity {
    private Spinner spinner_planType;
    private Spinner spinner_planDifficulty;
    private Spinner spinner_planDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_training_plan);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tworzenie planu");

        spinner_planType = (Spinner) findViewById(R.id.spinner_planType);
        spinner_planDifficulty = (Spinner) findViewById(R.id.spinner_planDifficulty);
        spinner_planDuration = (Spinner) findViewById(R.id.spinner_planDuration);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

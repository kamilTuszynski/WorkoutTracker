package com.workouttracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.workouttracker.models.TrainingDay;
import com.workouttracker.models.TrainingPlan;
import com.workouttracker.models.TrainingWeek;

import java.util.ArrayList;
import java.util.List;

public class CreateTrainingPlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_training_plan);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tworzenie planu");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void createTrainingPlan(View v)
    {
        boolean validatedCorrectly = true;
        EditText planNameEditText = (EditText) findViewById(R.id.editText_planName);
        EditText planDescriptionEditText = (EditText) findViewById(R.id.editText_planDescription);
        Spinner spinner_planType = (Spinner) findViewById(R.id.spinner_planType);
        Spinner spinner_planDifficulty = (Spinner) findViewById(R.id.spinner_planDifficulty);
        Spinner spinner_planDuration = (Spinner) findViewById(R.id.spinner_planDuration);

        if(planNameEditText.getText().toString().trim().length() == 0)
        {
            planNameEditText.setError("Nazwa planu jest wymagana!");
            validatedCorrectly = false;
        }
        if(planDescriptionEditText.getText().toString().trim().length() == 0)
        {
            planDescriptionEditText.setError("Opis planu jest wymagany!");
            validatedCorrectly = false;
        }

        if(validatedCorrectly)
        {
            final String name = planNameEditText.getText().toString().trim();
            String description = planDescriptionEditText.getText().toString();
            String type = spinner_planType.getSelectedItem().toString();
            String difficulty = spinner_planDifficulty.getSelectedItem().toString();
            int duration = Integer.parseInt(spinner_planDuration.getSelectedItem().toString());

            TrainingPlan plan = new TrainingPlan(name,description,type,difficulty,duration);
            final List<TrainingWeek> weeks = new ArrayList<TrainingWeek>();
            List<TrainingDay> days = new ArrayList<TrainingDay>();
            for(int i=1; i<=duration; i++){
                weeks.add(new TrainingWeek(i));
            }
            for(int j=1; j<=7; j++){
                days.add(new TrainingDay(j, false));
            }

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            WriteBatch batch = db.batch();
            final DocumentReference planRef = db.collection("trainingPlans").document();
            batch.set(planRef, plan);
            for (TrainingWeek week:weeks) {
                DocumentReference weekRef = planRef.collection("trainingWeeks")
                        .document(String.valueOf(week.getWeekNumber()));
                batch.set(weekRef, week);

                for (TrainingDay day:days) {
                    batch.set(weekRef.collection("trainingDays")
                            .document(String.valueOf(day.getDayNumber())), day);
                }
            }

            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    String planId = planRef.getId();

                    Intent i = new Intent(CreateTrainingPlanActivity.this, TrainingWeeksActivity.class);
                    i.putExtra("planId", planId);
                    i.putExtra("planName", name);
                    startActivity(i);
                    finish();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateTrainingPlanActivity.this,
                            "Dodawanie planu nie powiodło się", Toast.LENGTH_LONG).show();
                }
            });

        }

    }
}

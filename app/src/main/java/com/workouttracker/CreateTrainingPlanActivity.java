package com.workouttracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.workouttracker.models.Exercise;
import com.workouttracker.models.ExerciseSets;
import com.workouttracker.models.SetParameters;
import com.workouttracker.models.TrainingDay;
import com.workouttracker.models.TrainingPlan;
import com.workouttracker.models.TrainingWeek;

import java.util.ArrayList;
import java.util.Arrays;

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
        EditText planDescriptionEditText = (EditText) findViewById(R.id.editText_planName);
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
            String name = planNameEditText.getText().toString().trim();
            String description = planDescriptionEditText.getText().toString();
            String type = spinner_planType.getSelectedItem().toString();
            String difficulty = spinner_planDifficulty.getSelectedItem().toString();
            int duration = Integer.parseInt(spinner_planDuration.getSelectedItem().toString());

            SetParameters params = new SetParameters(100,5,9);
            Exercise exercise = new Exercise("Przysiad",false,false,false,false);

            SetParameters params2 = new SetParameters(50,10,8);
            Exercise exercise2 = new Exercise("Klata",false,false,false,false);

            ExerciseSets firstExercise = new ExerciseSets(exercise,new ArrayList<SetParameters>(
                    Arrays.asList(params,params,params)));
            ExerciseSets secondExercise = new ExerciseSets(exercise2,new ArrayList<SetParameters>(
                    Arrays.asList(params2,params2,params2)));

            TrainingDay firstDay = new TrainingDay(1, new ArrayList<ExerciseSets>(
                    Arrays.asList(firstExercise, secondExercise, secondExercise)
            ));

            TrainingDay fourthDay = new TrainingDay(4, new ArrayList<ExerciseSets>(
                    Arrays.asList(secondExercise, firstExercise)
            ));

            TrainingWeek firstWeek = new TrainingWeek(1, new ArrayList<TrainingDay>(
                    Arrays.asList(firstDay, fourthDay)));

            TrainingPlan plan = new TrainingPlan(name,description,type,difficulty,duration,
                    new ArrayList<TrainingWeek>(Arrays.asList(firstWeek, firstWeek)));

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("trainingPlans").add(plan)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
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

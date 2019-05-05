package com.workouttracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.workouttracker.models.Exercise;

public class AddExerciseAcitvity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Dodaj ćwiczenie");
    }

    public void addExercise(View v) {
        EditText exerciseNameEditText = (EditText) findViewById(R.id.et_exerciseName);
        if(exerciseNameEditText.getText().toString().trim().length() == 0)
        {
            exerciseNameEditText.setError("Nazwa ćwiczenia jest wymagana!");
        }
        else
        {
            String exerciseName = exerciseNameEditText.getText().toString().trim();
            Switch repsPossible = (Switch) findViewById(R.id.switch_repsPossible);
            Switch weightPossible = (Switch) findViewById(R.id.switch_weightPossible);
            Switch timePossible = (Switch) findViewById(R.id.switch_timePossible);
            Switch distancePossible = (Switch) findViewById(R.id.switch_distancePossible);

            Exercise exercise = new Exercise(exerciseName, repsPossible.isChecked(),
                    weightPossible.isChecked(), timePossible.isChecked(), distancePossible.isChecked());

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("exercises").add(exercise)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddExerciseAcitvity.this,
                                    "Dodawanie ćwiczenia nie powiodło się", Toast.LENGTH_LONG).show();
                        }
                    });

        }
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

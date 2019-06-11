package com.workouttracker.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.workouttracker.R;
import com.workouttracker.models.Exercise;
import com.workouttracker.models.WorkoutSet;

public class CreateWorkoutSetDialog extends DialogFragment {

    private EditText editTextWeight;
    private EditText editTextReps;
    private Spinner spinnerRpe;
    private Spinner spinnerExerciseName;
    private String setsRefPath;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference setsRef;

    public void setSetsRefPath(String setsRefPath) {
        this.setsRefPath = setsRefPath;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_workout_set, null);

        builder.setView(view)
                .setTitle("Dodaj serię")
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createWorkoutSet();
                    }
                });

        editTextWeight = view.findViewById(R.id.dialogCreateWorkoutSet_editText_weight);
        editTextReps = view.findViewById(R.id.dialogCreateWorkoutSet_editText_reps);
        spinnerRpe = view.findViewById(R.id.dialogCreateWorkoutSet_spinner_rpe);
        spinnerExerciseName = view.findViewById(R.id.dialogCreateWorkoutSet_spinner_exerciseName);
        setsRef = db.collection(setsRefPath);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExerciseName.setAdapter(adapter);

        db.collection("exercises").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Exercise exercise = document.toObject(Exercise.class);
                                adapter.add(exercise.getName());
                            }
                            spinnerExerciseName.setSelection(1);
                        }
                        else{

                        }
                    }
                });

        return builder.create();
    }

    private void createWorkoutSet()
    {
        boolean validatedCorrectly = true;

        if(editTextReps.getText().toString().trim().length() == 0){
            editTextReps.setError("Podanie liczby powtórzeń jest wymagane!");
            validatedCorrectly = false;
        }

        if(validatedCorrectly){
            String name = spinnerExerciseName.getSelectedItem().toString();
            float weight = Float.parseFloat(editTextWeight.getText().toString());
            int reps = Integer.parseInt(editTextReps.getText().toString());
            float rpe = Float.parseFloat(spinnerRpe.getSelectedItem().toString());
            long currentTimeMillis = System.currentTimeMillis();

            WorkoutSet set = new WorkoutSet(name,weight,reps,rpe,currentTimeMillis);
            setsRef.add(set);
        }
    }
}

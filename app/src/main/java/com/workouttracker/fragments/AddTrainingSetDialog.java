package com.workouttracker.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
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
import com.workouttracker.models.TrainingSet;


public class AddTrainingSetDialog extends AppCompatDialogFragment {
    private EditText editTextReps;
    private EditText editTextRpe;
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
        View view = inflater.inflate(R.layout.dialog_add_training_set, null);

        builder.setView(view)
                .setTitle("Dodaj serię")
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addTrainingSetToFirestore();
                    }
                });

        editTextReps = view.findViewById(R.id.editText_reps);
        editTextRpe = view.findViewById(R.id.editText_rpe);
        spinnerExerciseName = view.findViewById(R.id.spinner_exerciseName);
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

    private void addTrainingSetToFirestore()
    {
        String exerciseName = spinnerExerciseName.getSelectedItem().toString();

        if(editTextReps.getText().toString().trim().length() == 0){
            editTextReps.setError("Podanie liczby powtórzeń jest wymagane!");
        }
        else{
            int reps = Integer.parseInt(editTextReps.getText().toString());
            int rpe;

            try {
                rpe = Integer.parseInt(editTextRpe.getText().toString());
            }
            catch (NumberFormatException e){
                rpe = 0;
            }

            long currentTimeMillis = System.currentTimeMillis();
            TrainingSet set = new TrainingSet(exerciseName, reps, rpe, currentTimeMillis);
            setsRef.add(set);
        }
    }
}

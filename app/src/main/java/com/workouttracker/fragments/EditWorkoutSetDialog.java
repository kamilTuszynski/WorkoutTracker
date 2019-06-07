package com.workouttracker.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.workouttracker.R;
import com.workouttracker.models.WorkoutSet;

public class EditWorkoutSetDialog extends AppCompatDialogFragment {

    private EditText editTextWeight;
    private EditText editTextReps;
    private EditText editTextRpe;
    private TextView textViewExerciseName;
    private String setRefPath;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference setRef;

    public void setSetRefPath(String setRefPath) {
        this.setRefPath = setRefPath;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_workout_set, null);

        builder.setView(view)
                .setTitle("Edytuj serię")
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateWorkoutSet();
                    }
                });

        editTextWeight = view.findViewById(R.id.dialogEditWorkoutSet_editText_weight);
        editTextReps = view.findViewById(R.id.dialogEditWorkoutSet_editText_reps);
        editTextRpe = view.findViewById(R.id.dialogEditWorkoutSet_editText_rpe);
        textViewExerciseName = view.findViewById(R.id.dialogEditWorkoutSet_textView_exerciseName);
        setRef = db.document(setRefPath);

        setRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document =  task.getResult();
                    if(document.exists()){
                        WorkoutSet workoutSet = document.toObject(WorkoutSet.class);
                        editTextWeight.setText(String.valueOf(workoutSet.getWeight()));
                        editTextReps.setText(String.valueOf(workoutSet.getReps()));
                        editTextRpe.setText(String.valueOf(workoutSet.getRpe()));
                        textViewExerciseName.setText(workoutSet.getExerciseName());


                    } else {

                    }
                } else {

                }
            }
        });

        return builder.create();
    }

    private void updateWorkoutSet()
    {

    }
}

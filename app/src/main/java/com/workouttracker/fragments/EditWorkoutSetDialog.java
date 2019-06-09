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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.workouttracker.R;
import com.workouttracker.models.WorkoutSet;
import com.workouttracker.util.StringUtils;

public class EditWorkoutSetDialog extends AppCompatDialogFragment {

    private EditText editTextWeight;
    private EditText editTextReps;
    private Spinner spinnerRpe;
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
        spinnerRpe = view.findViewById(R.id.dialogEditWorkoutSet_spinner_rpe);
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

                        ArrayAdapter adapter = (ArrayAdapter) spinnerRpe.getAdapter();
                        String s = StringUtils.floatToStringWithoutTrailingZeros(workoutSet.getRpe());
                        int position = adapter.getPosition(s);

                        spinnerRpe.setSelection(position);

//                        String[] rpeArray = getResources().getStringArray(R.array.array_rpe);
//                        for (int i = 0;i < rpeArray.length; i++) {
//                            if(String.valueOf(workoutSet.getRpe()).equals(rpeArray[i])){
//                                spinnerRpe.setSelection(i);
//                            }
//                        }
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
        boolean validatedCorrectly = true;

        if(editTextReps.getText().toString().trim().length() == 0){
            editTextReps.setError("Podanie liczby powtórzeń jest wymagane!");
            validatedCorrectly = false;
        }
        if(editTextWeight.getText().toString().trim().length() == 0){
            editTextWeight.setError("Podanie liczby powtórzeń jest wymagane!");
            validatedCorrectly = false;
        }

        if(validatedCorrectly){
            float weight = Float.parseFloat(editTextWeight.getText().toString());
            int reps = Integer.parseInt(editTextReps.getText().toString());
            float rpe = Float.parseFloat(spinnerRpe.getSelectedItem().toString());

            setRef.update("weight", weight);
            setRef.update("reps", reps);
            setRef.update("rpe", rpe);
        }
    }
}

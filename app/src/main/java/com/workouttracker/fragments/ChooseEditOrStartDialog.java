package com.workouttracker.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.workouttracker.TrainingWeeksActivity;
import com.workouttracker.models.TrainingSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseEditOrStartDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private String planId;
    private String name;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef;
    private DocumentReference planRef;

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Wybierz opcję")
                .setPositiveButton("Zacznij", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH);
                        int day = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog
                                (getActivity(), ChooseEditOrStartDialog.this,year,month,day);
                        datePickerDialog.show();
                    }
                })
                .setNeutralButton("Przeglądaj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getActivity(), TrainingWeeksActivity.class);
                        i.putExtra("planId", planId);
                        i.putExtra("planName", name);
                        startActivity(i);
                    }
                })
                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }

    private void addWorkoutsToFirestore(){
        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> user = new HashMap<>();
        user.put("uid", userUID);

        userRef = db.collection("users").document(userUID);

        userRef.set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        planRef = db.collection("trainingPlans").document(planId);

        final List<TrainingSet> trainingSets = new ArrayList<TrainingSet>();

        planRef.collection("trainingWeeks").orderBy("weekNumber").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().collection("trainingDays").orderBy("dayNumber").get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        document.getReference().collection("trainingSets").orderBy("timeMillis").get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if(task.isSuccessful()){
                                                                            for (QueryDocumentSnapshot document : task.getResult()){
                                                                                TrainingSet set = document.toObject(TrainingSet.class);
                                                                                trainingSets.add(set);
                                                                            }
                                                                        } else {
                                                                            return;
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                } else {
                                                    return;
                                                }
                                            }
                                        });
                            }
                        } else {
                            return;
                        }
                }});

        for(TrainingSet set : trainingSets){
            userRef.collection("workouts").add(set);
        }


        userRef.collection("workouts").add(user);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);



        addWorkoutsToFirestore();
    }
}

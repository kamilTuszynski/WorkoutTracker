package com.workouttracker.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.workouttracker.TrainingWeeksActivity;
import com.workouttracker.models.TrainingSet;
import com.workouttracker.models.Workout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseEditOrStartDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private String planId;
    private String name;
    private int duration;

    private Context context;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef;
    private DocumentReference planRef;

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setContext(Context context) {
        this.context = context;
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

    private void addWorkoutsToFirestore(final Calendar calendar){
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

        ArrayList<Task<?>> tasks = new ArrayList<>();

        for(int week=1; week<= duration;week++){
            for(int day=1;day<=7;day++){
                Query query = planRef.collection("trainingWeeks")
                        .document(String.valueOf(week)).collection("trainingDays")
                        .document(String.valueOf(day)).collection("trainingSets").orderBy("timeMillis");
                Task task = query.get();
                tasks.add(task);
            }
        }

        Task<List<QuerySnapshot>> allTasks = Tasks.whenAllSuccess(tasks);
        allTasks.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {
                List<List<TrainingSet>> trainingDays = new ArrayList<>();

                for(QuerySnapshot queryDocumentSnapshots : querySnapshots){
                    List<TrainingSet> trainingSets = new ArrayList<>();
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        TrainingSet set = documentSnapshot.toObject(TrainingSet.class);
                        trainingSets.add(set);
                    }
                    trainingDays.add(trainingSets);

                }

                WriteBatch batch = db.batch();

                calendar.set(Calendar.HOUR, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date date = calendar.getTime();

                for(List<TrainingSet> trainingDay : trainingDays){
                    Workout workout = new Workout(date, null, null, null, null);

                    Calendar c = Calendar.getInstance();
                    c.setTime(date);
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    String id = String.valueOf(year) + String.valueOf(month) + String.valueOf(day);

                    c.add(Calendar.DATE, 1);
                    date = c.getTime();

                    if (trainingDay.size() != 0){
                        DocumentReference workoutRef = userRef.collection("workouts").document(id);
                        batch.set(workoutRef, workout);

                        for(TrainingSet set : trainingDay){
                            DocumentReference setRef = workoutRef.collection("sets").document();
                            batch.set(setRef, set);
                        }
                    }
                }

                batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        ((Activity) context).finish();
                    }
                });
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        addWorkoutsToFirestore(c);
    }
}



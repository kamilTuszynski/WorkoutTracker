package com.workouttracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.workouttracker.adapters.TrainingWeekAdapter;
import com.workouttracker.models.TrainingWeek;

public class TrainingWeeksActivity extends AppCompatActivity {

    private TrainingWeekAdapter adapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference plansRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_weeks);

        setUpRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    private void setUpRecyclerView() {
        Intent intent = getIntent();
        String planId = intent.getExtras().getString("planId");

        plansRef = db.collection("trainingPlans/" + planId + "/trainingWeeks");
        Query query = plansRef.orderBy("weekNumber", Query.Direction.ASCENDING);

//        plansRef.get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
//                            TrainingWeek week = documentSnapshot.toObject(TrainingWeek.class);
//                        }
//                    }
//                });

        FirestoreRecyclerOptions<TrainingWeek> options = new FirestoreRecyclerOptions.Builder<TrainingWeek>()
                .setQuery(query, TrainingWeek.class)
                .build();

        adapter = new TrainingWeekAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recView_trainingWeeks);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
}

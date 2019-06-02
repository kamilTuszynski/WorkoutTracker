package com.workouttracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.workouttracker.adapters.TrainingPlanAdapter;
import com.workouttracker.fragments.ChooseEditOrStartDialog;
import com.workouttracker.models.TrainingPlan;

public class ShowTrainingPlansActivity extends AppCompatActivity {

    private TrainingPlanAdapter adapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference plansRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_training_plans);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Dostępne plany");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_addTrainingPlan);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowTrainingPlansActivity.this, CreateTrainingPlanActivity.class);
                startActivity(intent);
            }
        });

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpRecyclerView() {
        plansRef = db.collection( "trainingPlans");
        Query query = plansRef.orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<TrainingPlan> options = new FirestoreRecyclerOptions.Builder<TrainingPlan>()
                .setQuery(query, TrainingPlan.class)
                .build();

        adapter = new TrainingPlanAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recView_trainingPlans);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new TrainingPlanAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                String planId = documentSnapshot.getId();
                String name = documentSnapshot.toObject(TrainingPlan.class).getName();
                int duration = documentSnapshot.toObject(TrainingPlan.class).getDurationInWeeks();

                ChooseEditOrStartDialog dialog = new ChooseEditOrStartDialog();
                dialog.setPlanId(planId);
                dialog.setName(name);
                dialog.setDuration(duration);
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });
    }
}

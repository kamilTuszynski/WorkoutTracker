package com.workouttracker;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.workouttracker.adapters.TrainingSetAdapter;
import com.workouttracker.fragments.AddTrainingSetDialog;
import com.workouttracker.models.TrainingSet;

public class TrainingSetsActivity extends AppCompatActivity {
    private TrainingSetAdapter adapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference setsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_sets);

        String title = "Tydzień " + String.valueOf(getIntent().getExtras().getInt("weekNumber")) +
                " : Dzień " + String.valueOf(getIntent().getExtras().getInt("dayNumber"));

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        setUpRecyclerView();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_addTrainingSet);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setsRefPath = setsRef.getPath();
                AddTrainingSetDialog dialog = new AddTrainingSetDialog();
                dialog.setSetsRefPath(setsRefPath);
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        });


        final RecyclerView recViewTrainingSets = findViewById(R.id.recView_trainingSets);

        // TODO: ustawienie restDay w Firestore
        final CheckBox checkBoxRestDay = ( CheckBox ) findViewById(R.id.checkBox_isRestDay );
        checkBoxRestDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    int count = 0;
                    if(recViewTrainingSets.getAdapter() != null){
                        count = recViewTrainingSets.getAdapter().getItemCount();
                    }
                    if(count == 0){
                        fab.setEnabled(false);
                        fab.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    }
                    else{
                        checkBoxRestDay.setChecked(false);
                    }

                }
                else{
                    fab.setEnabled(true);
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                }
            }
        });


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
        Intent intent = getIntent();
        String path = intent.getExtras().getString("path");

        setsRef = db.collection(path + "/trainingSets");
        Query query = setsRef.orderBy("timeMillis", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<TrainingSet> options = new FirestoreRecyclerOptions.Builder<TrainingSet>()
                .setQuery(query, TrainingSet.class)
                .build();

        adapter = new TrainingSetAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recView_trainingSets);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new TrainingSetAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                Toast.makeText(TrainingSetsActivity.this, id, Toast.LENGTH_LONG).show();
            }
        });
    }
}

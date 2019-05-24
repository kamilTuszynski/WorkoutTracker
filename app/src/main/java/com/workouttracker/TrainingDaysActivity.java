package com.workouttracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.workouttracker.adapters.TrainingDayAdapter;
import com.workouttracker.models.TrainingDay;

public class TrainingDaysActivity extends AppCompatActivity {
    private TrainingDayAdapter adapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference daysRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_days);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Dni tygodnia");

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
        Intent intent = getIntent();
        String path = intent.getExtras().getString("path");

        daysRef = db.collection(path + "/trainingDays");
        Query query = daysRef.orderBy("dayNumber", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<TrainingDay> options = new FirestoreRecyclerOptions.Builder<TrainingDay>()
                .setQuery(query, TrainingDay.class)
                .build();

        adapter = new TrainingDayAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recView_trainingDays);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new TrainingDayAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                Toast.makeText(TrainingDaysActivity.this, id, Toast.LENGTH_LONG).show();
            }
        });
    }
}

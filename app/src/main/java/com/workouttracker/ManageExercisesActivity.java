package com.workouttracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.workouttracker.adapters.ExerciseAdapter;
import com.workouttracker.models.Exercise;

public class ManageExercisesActivity extends AppCompatActivity {
    private ExerciseAdapter adapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference exercisesRef = db.collection("exercises");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_exercises);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Zarządzanie ćwiczeniami");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddExercise);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageExercisesActivity.this, AddExerciseAcitvity.class);
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
        Query query = exercisesRef.orderBy("name", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Exercise> options = new FirestoreRecyclerOptions.Builder<Exercise>()
                .setQuery(query, Exercise.class)
                .build();

        adapter = new ExerciseAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recView_exercises);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

    }
}

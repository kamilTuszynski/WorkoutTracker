package com.workouttracker.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.workouttracker.R;
import com.workouttracker.adapters.WorkoutSetAdapter;
import com.workouttracker.models.TrainingSet;

import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class WorkoutsFragment extends Fragment {

    private HorizontalCalendar horizontalCalendar;

    private WorkoutSetAdapter adapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference workoutsRef;

    private RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workouts, container, false);



        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                int year = date.get(Calendar.YEAR);
                int month = date.get(Calendar.MONTH);
                int day = date.get(Calendar.DAY_OF_MONTH);
                String id = String.valueOf(year) + String.valueOf(month) + String.valueOf(day);

                String id2 = "201952";

                Query query = workoutsRef.document(id).collection("sets")
                        .orderBy("timeMillis", Query.Direction.ASCENDING);

                FirestoreRecyclerOptions<TrainingSet> options = new FirestoreRecyclerOptions.Builder<TrainingSet>()
                        .setQuery(query, TrainingSet.class)
                        .build();


                adapter.stopListening();
                adapter = new WorkoutSetAdapter(options);
                recyclerView.swapAdapter(adapter, false);
                adapter.startListening();
            }
        });

//        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
//            @Override
//            public void onDateSelected(Calendar date, int position) {
//                int year = date.get(Calendar.YEAR);
//                int month = date.get(Calendar.MONTH);
//                int day = date.get(Calendar.DAY_OF_MONTH);
//                String id = String.valueOf(year) + String.valueOf(month) + String.valueOf(day);
//
//                Query query = workoutsRef.document(id).collection("sets")
//                        .orderBy("timeMillis", Query.Direction.ASCENDING);
//
//                FirestoreRecyclerOptions<TrainingSet> options = new FirestoreRecyclerOptions.Builder<TrainingSet>()
//                        .setQuery(query, TrainingSet.class)
//                        .build();
//
//                query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        for(DocumentSnapshot document : queryDocumentSnapshots){
//                            TrainingSet set = document.toObject(TrainingSet.class);
//                            String s = set.getExerciseName();
//                        }
//                    }
//                });
//
//                adapter = new WorkoutSetAdapter(options);
//                recyclerView.setAdapter(adapter);
//
//                int count = recyclerView.getAdapter().getItemCount();
//                String s = String.valueOf(count);
//            }
//        });

        setUpRecyclerView(view);
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

    private void setUpRecyclerView(View view) {
        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        workoutsRef = db.collection("users").document(userUID).collection("workouts");

        Calendar c = horizontalCalendar.getSelectedDate();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String id = String.valueOf(year) + String.valueOf(month) + String.valueOf(day);

        String id2 = "201952";

        Query query = workoutsRef.document(id).collection("sets")
                .orderBy("timeMillis", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<TrainingSet> options = new FirestoreRecyclerOptions.Builder<TrainingSet>()
                .setQuery(query, TrainingSet.class)
                .build();

        adapter = new WorkoutSetAdapter(options);

        recyclerView = view.findViewById(R.id.recView_workoutSets);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

//        adapter.setOnItemClickListener(new WorkoutSetAdapter.OnItemClickListener() {
//            @Override
//            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
//                String id = documentSnapshot.getId();
//                Toast.makeText(getActivity(), id, Toast.LENGTH_LONG).show();
//            }
//        });
    }
}

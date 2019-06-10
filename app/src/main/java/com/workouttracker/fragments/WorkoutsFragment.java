package com.workouttracker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.workouttracker.MapsActivity;
import com.workouttracker.R;
import com.workouttracker.adapters.WorkoutSetAdapter;
import com.workouttracker.models.Workout;
import com.workouttracker.models.WorkoutSet;

import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class WorkoutsFragment extends Fragment {

    private HorizontalCalendar horizontalCalendar;
    private RecyclerView recyclerView;
    private TextView textViewPlaceName;

    private WorkoutSetAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference workoutsRef;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workouts, container, false);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        workoutsRef = db.collection("users").document(userUID).collection("workouts");

        textViewPlaceName = view.findViewById(R.id.textView_placeName);

        view.findViewById(R.id.btn_changeLocation)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChangeWorkoutLocationAcitivity();
            }
        });

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -2);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 4);

        horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .defaultSelectedDate(Calendar.getInstance())
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                setUpRecyclerView(view, date);
            }
        });

        setUpRecyclerView(view, Calendar.getInstance());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_today) {
            horizontalCalendar.selectDate(Calendar.getInstance(), false);
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private void setUpRecyclerView(final View view, Calendar date) {
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        String id = String.valueOf(year) + String.valueOf(month) + String.valueOf(day);

        workoutsRef.document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot document
                    , @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                if (document != null && document.exists()) {
                    textViewPlaceName.setVisibility(View.VISIBLE);
                    view.findViewById(R.id.btn_changeLocation).setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Workout workout = document.toObject(Workout.class);
                    textViewPlaceName.setText(workout.getPlaceName() + ": " + workout.getPlaceAddress());
                }
                else{
                    textViewPlaceName.setVisibility(View.GONE);
                    view.findViewById(R.id.btn_changeLocation).setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    return;
                }
            }
        });

        Query query = workoutsRef.document(id).collection("sets")
                .orderBy("timeMillis", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<WorkoutSet> options = new FirestoreRecyclerOptions.Builder<WorkoutSet>()
                .setQuery(query, WorkoutSet.class)
                .build();

        if(adapter != null)
            adapter.stopListening();
        adapter = new WorkoutSetAdapter(options);

        recyclerView = view.findViewById(R.id.recView_workoutSets);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new WorkoutSetAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                EditWorkoutSetDialog dialog = new EditWorkoutSetDialog();
                dialog.setSetRefPath(documentSnapshot.getReference().getPath());
                dialog.show(getFragmentManager(), "dialog");
            }
        });
        adapter.startListening();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());
                showSnackbar();
            }
        }).attachToRecyclerView(recyclerView);
    }

    private void startChangeWorkoutLocationAcitivity(){
        Intent intent = new Intent(getActivity(), MapsActivity.class);
        Calendar date = horizontalCalendar.getSelectedDate();
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        String id = String.valueOf(year) + String.valueOf(month) + String.valueOf(day);
        intent.putExtra("workoutRefPath", workoutsRef.document(id).getPath());
        startActivity(intent);
    }

    private void showSnackbar() {
        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Seria usunięta",
                Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}

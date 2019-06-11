package com.workouttracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.workouttracker.models.Workout;
import com.workouttracker.models.WorkoutSet;
import com.workouttracker.util.DateE1RM;
import com.workouttracker.util.RPEUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class E1RMChartActivity extends AppCompatActivity {
    private String exerciseName;

    private int yearFrom;
    private int monthFrom;
    private int dayFrom;

    private int yearTo;
    private int monthTo;
    private int dayTo;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference workoutsRef;

    List<DateE1RM> e1RMsWithDate;

    GraphView graph;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e1_rmchart);

        exerciseName = getIntent().getExtras().get("exerciseName").toString();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("E1RM: " + exerciseName);

        e1RMsWithDate = new ArrayList<>();

        String userUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        workoutsRef = db.collection("users").document(userUID).collection("workouts");



        yearFrom = getIntent().getIntExtra("yearFrom", 0);
        monthFrom = getIntent().getIntExtra("monthFrom", 0);
        dayFrom = getIntent().getIntExtra("dayFrom", 0);
        Calendar calFrom = Calendar.getInstance();
        calFrom.set(Calendar.YEAR, yearFrom);
        calFrom.set(Calendar.MONTH, monthFrom);
        calFrom.set(Calendar.DAY_OF_MONTH, dayFrom);

        yearTo = getIntent().getIntExtra("yearTo", 0);
        monthTo = getIntent().getIntExtra("monthTo", 0);
        dayTo = getIntent().getIntExtra("dayTo", 0);
        Calendar calTo = Calendar.getInstance();
        calTo.set(Calendar.YEAR, yearTo);
        calTo.set(Calendar.MONTH, monthTo);
        calTo.set(Calendar.DAY_OF_MONTH, dayTo);

        graph = (GraphView) findViewById(R.id.graph);
        graph.setVisibility(View.VISIBLE);

        // TODO: jak pobrac dane dla wielu treningow i dla wielu serii wewnatrz treningu ???
        workoutsRef.whereGreaterThanOrEqualTo("date", calFrom.getTime())
                .whereLessThanOrEqualTo("date", calTo.getTime()).orderBy("date", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    // przeszukiwanie treningow
                    for (QueryDocumentSnapshot document : task.getResult()){
                        final Workout workout = document.toObject(Workout.class);

                        document.getReference().collection("sets").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    double e1RMBiggest = 0;
                                    // przeszukiwanie serii wewnatrz treningu
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        WorkoutSet set = document.toObject(WorkoutSet.class);
                                        String exName = set.getExerciseName();
                                        if (exName.equals(exerciseName)){
                                            double e1RM = RPEUtil.calculateE1RM(set.getRpe(), set.getReps(), set.getWeight());
                                            if(e1RM > e1RMBiggest){
                                                e1RMBiggest = e1RM;
                                            }
                                        }
                                    }
                                    if(e1RMBiggest != 0){
                                        // zapisanie do jakiejs kolekcji
                                        e1RMsWithDate.add(new DateE1RM(workout.getDate(), e1RMBiggest));
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }


    public void draw(View v){
        DataPoint[] dp = new DataPoint[e1RMsWithDate.size()];

        if (e1RMsWithDate.size() > 0) {
            Collections.sort(e1RMsWithDate, new Comparator<DateE1RM>() {
                @Override
                public int compare(final DateE1RM object1, final DateE1RM object2) {
                    return object1.date.compareTo(object2.date);
                }
            });
        }

        int i = 0;
        for (DateE1RM dateE1RM : e1RMsWithDate){
            Date date = dateE1RM.date;
            Double e1RM = dateE1RM.E1RM;

            long l = date.getTime();
            dp[i] = new DataPoint(date, e1RM);
            i++;
        }


        LineGraphSeries <DataPoint> series = new LineGraphSeries<>(dp);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);


        // set date label formatter
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(E1RMChartActivity.this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

// set manual x bounds to have nice steps
        graph.getViewport().setMinX(e1RMsWithDate.get(0).date.getTime());
        graph.getViewport().setMaxX(e1RMsWithDate.get(e1RMsWithDate.size() - 1).date.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getViewport().setScrollable(true);


        graph.addSeries(series);
    }
}

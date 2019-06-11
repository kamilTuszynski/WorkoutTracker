package com.workouttracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.workouttracker.models.Exercise;

import java.util.Calendar;

public class ChoosePlanStatisticActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private TextView textViewDateFrom;
    private TextView textViewDateTo;
    private Spinner spinnerChartType;
    private Spinner spinnerExercise;

    private String idDateFrom;
    private int yearFrom;
    private int monthFrom;
    private int dayFrom;

    private String idDateTo;
    private int yearTo;
    private int monthTo;
    private int dayTo;

    private DatePickerDialog.OnDateSetListener fromDateListener,toDateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_plan_statistic);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Wybierz wykres");

        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);

        String formattedDate = getFormattedDate(day, month, year);
        idDateFrom = String.valueOf(year) + String.valueOf(month) + String.valueOf(day);
        idDateTo = String.valueOf(year) + String.valueOf(month) + String.valueOf(day);

        yearFrom = year;
        monthFrom = month;
        dayFrom = day;
        yearTo = year;
        monthTo = month;
        dayTo = day;

        textViewDateFrom = findViewById(R.id.textView_dateFrom);
        textViewDateTo = findViewById(R.id.textView_dateTo);
        spinnerChartType = findViewById(R.id.spinner_chartType);
        spinnerExercise = findViewById(R.id.spinner_exercise);

        textViewDateFrom.setText(formattedDate);
        textViewDateTo.setText(formattedDate);

        textViewDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog
                        (ChoosePlanStatisticActivity.this, fromDateListener,year,month,day);
                datePickerDialog.show();
            }
        });

        textViewDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog
                        (ChoosePlanStatisticActivity.this, toDateListener,year,month,day);
                datePickerDialog.show();
            }
        });


        fromDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                textViewDateFrom.setText(getFormattedDate(dayOfMonth,month,year));
                idDateFrom = String.valueOf(year) + String.valueOf(month) + String.valueOf(dayOfMonth);
                yearFrom = year;
                monthFrom = month;
                dayFrom = dayOfMonth;
            }
        };
        toDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                textViewDateTo.setText(getFormattedDate(dayOfMonth,month,year));
                idDateTo = String.valueOf(year) + String.valueOf(month) + String.valueOf(dayOfMonth);
                yearTo = year;
                monthTo = month;
                dayTo = dayOfMonth;
            }
        };



        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExercise.setAdapter(adapter);


        db.collection("exercises").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Exercise exercise = document.toObject(Exercise.class);
                                adapter.add(exercise.getName());
                            }
                            spinnerExercise.setSelection(0);
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

    public void showChart(View v){
        if(textViewDateFrom.getText().toString().trim().length() == 0){
            textViewDateFrom.setError("Podanie początkowej daty jest wymagane!");
            return;
        }
        if(textViewDateTo.getText().toString().trim().length() == 0){
            textViewDateTo.setError("Podanie końcowej daty jest wymagane!");
            return;
        }

        Calendar calFrom = Calendar.getInstance();
        calFrom.set(Calendar.YEAR, yearFrom);
        calFrom.set(Calendar.MONTH, monthFrom);
        calFrom.set(Calendar.DAY_OF_MONTH, dayFrom);

        Calendar calTo = Calendar.getInstance();
        calTo.set(Calendar.YEAR, yearTo);
        calTo.set(Calendar.MONTH, monthTo);
        calTo.set(Calendar.DAY_OF_MONTH, dayTo);

        if(calFrom.getTime().getTime() >= calTo.getTime().getTime()){
            textViewDateFrom.setError("Początkowa data musi być wcześniejsza niż końcowa!");
            return;
        }

        Intent intent = new Intent(ChoosePlanStatisticActivity.this, E1RMChartActivity.class);
        intent.putExtra("yearFrom", yearFrom);
        intent.putExtra("monthFrom", monthFrom);
        intent.putExtra("dayFrom", dayFrom);

        intent.putExtra("yearTo", yearTo);
        intent.putExtra("monthTo", monthTo);
        intent.putExtra("dayTo", dayTo);

        intent.putExtra("exerciseName", spinnerExercise.getSelectedItem().toString());
        startActivity(intent);

    }

    private String getFormattedDate(int dayOfMonth, int month, int year){
        String monthS = String.valueOf(month + 1);
        String dayS = String.valueOf(dayOfMonth);

        if(month < 10){

            monthS = "0" + String.valueOf(month + 1);
        }
        if(dayOfMonth < 10){

            dayS  = "0" + String.valueOf(dayOfMonth) ;
        }
        String result = dayS + "-" + monthS + "-" + year;

        return result;
    }
}

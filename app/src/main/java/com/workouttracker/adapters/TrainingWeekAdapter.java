package com.workouttracker.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.workouttracker.R;
import com.workouttracker.models.TrainingWeek;

public class TrainingWeekAdapter extends FirestoreRecyclerAdapter<TrainingWeek, TrainingWeekAdapter.TrainingWeekHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TrainingWeekAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TrainingWeekHolder holder, int position, @NonNull TrainingWeek model) {

    }

    @NonNull
    @Override
    public TrainingWeekHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exercise_item,
                viewGroup, false);
        return new TrainingWeekHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class TrainingWeekHolder extends RecyclerView.ViewHolder{

        public TrainingWeekHolder(View itemView){
            super(itemView);
        }
    }
}

package com.workouttracker.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.workouttracker.R;
import com.workouttracker.models.Exercise;

public class ExerciseAdapter extends FirestoreRecyclerAdapter<Exercise, ExerciseAdapter.ExerciseHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ExerciseAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ExerciseHolder holder, int position, @NonNull Exercise model) {
        holder.textViewExerciseName.setText(model.getName());
        if(!model.isRepsPossible()){
            holder.imageViewReps.setImageResource(R.drawable.ic_close_black_24dp);
        }
        if(!model.isWeightPossible()){
            holder.imageViewWeight.setImageResource(R.drawable.ic_close_black_24dp);
        }
        if(!model.isTimePossible()){
            holder.imageViewTime.setImageResource(R.drawable.ic_close_black_24dp);
        }
        if(!model.isDistancePossible()){
            holder.imageViewDistance.setImageResource(R.drawable.ic_close_black_24dp);
        }
    }

    @NonNull
    @Override
    public ExerciseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.exercise_item,
                viewGroup, false);
        return new ExerciseHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class ExerciseHolder extends RecyclerView.ViewHolder{
        TextView textViewExerciseName;
        ImageView imageViewReps;
        ImageView imageViewWeight;
        ImageView imageViewTime;
        ImageView imageViewDistance;

        public ExerciseHolder(View itemView){
            super(itemView);
            textViewExerciseName = itemView.findViewById(R.id.textView_exerciseName);
            imageViewReps = itemView.findViewById(R.id.imageView_reps);
            imageViewWeight = itemView.findViewById(R.id.imageView_weight);
            imageViewTime = itemView.findViewById(R.id.imageView_time);
            imageViewDistance = itemView.findViewById(R.id.imageView_distance);
        }
    }
}

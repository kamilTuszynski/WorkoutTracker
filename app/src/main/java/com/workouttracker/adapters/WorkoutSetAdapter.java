package com.workouttracker.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.workouttracker.R;
import com.workouttracker.models.WorkoutSet;
import com.workouttracker.util.StringUtils;


public class WorkoutSetAdapter extends FirestoreRecyclerAdapter<WorkoutSet, WorkoutSetAdapter.WorkoutSetHolder> {

    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public WorkoutSetAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull WorkoutSetHolder holder, int position, @NonNull WorkoutSet model) {
        holder.textViewWorkoutSetExerciseName.setText(model.getExerciseName());
        holder.textViewWorkoutSetWeight.setText(StringUtils.floatToStringWithoutTrailingZeros(model.getWeight()) + " kg");
        holder.textViewWorkoutSetReps.setText(String.valueOf(model.getReps()) + " powt.");
        if(model.getRpe() != 0){
            holder.textViewWorkoutSetRpe.setText("@" + StringUtils.floatToStringWithoutTrailingZeros(model.getRpe()));
        }
        else {
            holder.textViewWorkoutSetRpe.setText("");
        }
    }

    @NonNull
    @Override
    public WorkoutSetHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.workout_set_item,
                viewGroup, false);
        return new WorkoutSetHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class WorkoutSetHolder extends RecyclerView.ViewHolder{
        TextView textViewWorkoutSetExerciseName;
        TextView textViewWorkoutSetWeight;
        TextView textViewWorkoutSetReps;
        TextView textViewWorkoutSetRpe;

        public WorkoutSetHolder(View itemView){
            super(itemView);
            textViewWorkoutSetExerciseName = itemView.findViewById(R.id.textView_workoutSetExerciseName);
            textViewWorkoutSetWeight = itemView.findViewById(R.id.textView_workoutSetWeight);
            textViewWorkoutSetReps = itemView.findViewById(R.id.textView_workoutSetReps);
            textViewWorkoutSetRpe = itemView.findViewById(R.id.textView_workoutSetRpe);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.OnItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}

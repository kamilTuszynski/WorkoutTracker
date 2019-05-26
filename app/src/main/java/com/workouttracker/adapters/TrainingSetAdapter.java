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
import com.workouttracker.models.TrainingSet;


public class TrainingSetAdapter extends FirestoreRecyclerAdapter<TrainingSet, TrainingSetAdapter.TrainingSetHolder> {

    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TrainingSetAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TrainingSetHolder holder, int position, @NonNull TrainingSet model) {
        holder.textViewTrainingSetExerciseName.setText(model.getExerciseName());
        holder.textViewTrainingSetReps.setText(String.valueOf(model.getReps()) + " powtórzeń");
        if(model.getRpe() != 0){
            holder.getTextViewTrainingSetRpe.setText("@" + String.valueOf(model.getRpe()));
        }
    }

    @NonNull
    @Override
    public TrainingSetHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.training_set_item,
                viewGroup, false);
        return new TrainingSetHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class TrainingSetHolder extends RecyclerView.ViewHolder{
        TextView textViewTrainingSetExerciseName;
        TextView textViewTrainingSetReps;
        TextView getTextViewTrainingSetRpe;

        public TrainingSetHolder(View itemView){
            super(itemView);
            textViewTrainingSetExerciseName = itemView.findViewById(R.id.textView_trainingSetExerciseName);
            textViewTrainingSetReps = itemView.findViewById(R.id.textView_trainingSetReps);
            getTextViewTrainingSetRpe = itemView.findViewById(R.id.textView_trainingSetRpe);

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

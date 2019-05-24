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
import com.workouttracker.models.TrainingDay;



public class TrainingDayAdapter extends FirestoreRecyclerAdapter<TrainingDay, TrainingDayAdapter.TrainingDayHolder> {

    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TrainingDayAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TrainingDayHolder holder, int position, @NonNull TrainingDay model) {
        holder.textViewTrainingDay.setText("Dzień " + String.valueOf(model.getDayNumber()));

    }

    @NonNull
    @Override
    public TrainingDayHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.training_day_item,
                viewGroup, false);
        return new TrainingDayHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class TrainingDayHolder extends RecyclerView.ViewHolder{
        TextView textViewTrainingDay;

        public TrainingDayHolder(View itemView){
            super(itemView);
            textViewTrainingDay = itemView.findViewById(R.id.textView_trainingDay);

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

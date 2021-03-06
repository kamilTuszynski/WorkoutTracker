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
import com.workouttracker.models.TrainingWeek;

public class TrainingWeekAdapter extends FirestoreRecyclerAdapter<TrainingWeek, TrainingWeekAdapter.TrainingWeekHolder> {

    private OnItemClickListener listener;

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
        holder.textViewTrainingWeek.setText("Tydzień " + String.valueOf(model.getWeekNumber()));

    }

    @NonNull
    @Override
    public TrainingWeekHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.training_week_item,
                viewGroup, false);
        return new TrainingWeekHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class TrainingWeekHolder extends RecyclerView.ViewHolder{
        TextView textViewTrainingWeek;

        public TrainingWeekHolder(View itemView){
            super(itemView);
            textViewTrainingWeek = itemView.findViewById(R.id.textView_trainingWeek);

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

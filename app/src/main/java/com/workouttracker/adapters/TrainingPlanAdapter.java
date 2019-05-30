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
import com.workouttracker.models.TrainingPlan;


public class TrainingPlanAdapter extends FirestoreRecyclerAdapter<TrainingPlan, TrainingPlanAdapter.TrainingPlanHolder> {

    private OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TrainingPlanAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TrainingPlanHolder holder, int position, @NonNull TrainingPlan model) {
        holder.textViewPlanName.setText(model.getName());
        holder.textViewPlanDifficulty.setText(model.getDifficulty());
        holder.textViewPlanDuration.setText(String.valueOf(model.getDurationInWeeks()) + " tyg.");
        holder.textViewPlanCategory.setText(model.getType());
        holder.textViewPlanDescription.setText(model.getDescription());
    }

    @NonNull
    @Override
    public TrainingPlanHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.training_plan_item,
                viewGroup, false);
        return new TrainingPlanHolder(v);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class TrainingPlanHolder extends RecyclerView.ViewHolder{
        TextView textViewPlanName;
        TextView textViewPlanDifficulty;
        TextView textViewPlanDuration;
        TextView textViewPlanCategory;
        TextView textViewPlanDescription;

        public TrainingPlanHolder(View itemView){
            super(itemView);
            textViewPlanName = itemView.findViewById(R.id.textView_planName);
            textViewPlanDifficulty = itemView.findViewById(R.id.textView_planDifficulty);
            textViewPlanDuration = itemView.findViewById(R.id.textView_planDuration);
            textViewPlanCategory = itemView.findViewById(R.id.textView_planCategory);
            textViewPlanDescription = itemView.findViewById(R.id.textView_planDescription);
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

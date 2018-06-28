package com.example.aaronbrecher.cookmeright.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aaronbrecher.cookmeright.databinding.StepListItemBinding;
import com.example.aaronbrecher.cookmeright.models.Step;
import com.example.aaronbrecher.cookmeright.ui.ListItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.StepListViewHolder> {

    private static final String TAG = StepListAdapter.class.getSimpleName();
    private List<Step> mSteps;
    private ListItemClickListener mClickListener;

    public StepListAdapter(List<Step> steps, ListItemClickListener clickListener) {
        mSteps = steps;
        mClickListener = clickListener;
    }

    @NonNull
    @Override
    public StepListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new StepListViewHolder(StepListItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StepListViewHolder holder, int position) {
        Step step = mSteps.get(position);
        String imageUrl = step.getThumbnailURL();
        if(imageUrl != null && !imageUrl.isEmpty()){
            Picasso.get().load(imageUrl).into(holder.binding.stepListItemImage);
        }
        holder.binding.stepListItemDescription.setText(step.getShortDescription());
        int stepNumber = step.getId()+1;
        holder.binding.stepListItemNumber.setText(String.format("Step %s", String.valueOf(stepNumber)));
    }

    @Override
    public int getItemCount() {
        if(mSteps == null) return 0;
        return mSteps.size();
    }

    public class StepListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private StepListItemBinding binding;
        public StepListViewHolder(StepListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Step step = mSteps.get(getAdapterPosition());
            mClickListener.onListItemClick(step);
        }
    }
}

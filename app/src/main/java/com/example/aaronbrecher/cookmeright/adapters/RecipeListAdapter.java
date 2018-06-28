package com.example.aaronbrecher.cookmeright.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aaronbrecher.cookmeright.databinding.RecipeListItemBinding;
import com.example.aaronbrecher.cookmeright.models.Recipe;
import com.example.aaronbrecher.cookmeright.ui.ListItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {
    private List<Recipe> mRecipes;
    private ListItemClickListener mClickListener;

    public RecipeListAdapter(List<Recipe> recipes, ListItemClickListener clickListener) {
        mRecipes = recipes;
        mClickListener = clickListener;
    }

    @NonNull
    @Override
    public RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new RecipeListViewHolder(RecipeListItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        String imageUrlString = recipe.getImage();
        if (imageUrlString != null && !imageUrlString.isEmpty()) {
            Picasso.get()
                    .load(imageUrlString)
                    .into(holder.mBinding.recipeListItemImage);
        }
        holder.mBinding.recipeListItemTitle.setText(recipe.getName());
        holder.mBinding.recipeListItemServings.setText(String.format("Servings: %s", String.valueOf(recipe.getServings())));
        holder.mBinding.recipeListItemNumSteps.setText(String.format("Amount of steps: %d", recipe.getSteps().size()));
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) return 0;
        return mRecipes.size();
    }

    public class RecipeListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RecipeListItemBinding mBinding;

        public RecipeListViewHolder(RecipeListItemBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Recipe recipe = mRecipes.get(getAdapterPosition());
            mClickListener.onListItemClick(recipe);
        }
    }

    public void swapLists(List<Recipe> recipes) {
        mRecipes = recipes;
    }
}

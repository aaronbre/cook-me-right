package com.example.aaronbrecher.cookmeright.ViewModels;

import android.arch.lifecycle.ViewModel;

import com.example.aaronbrecher.cookmeright.models.Ingredient;
import com.example.aaronbrecher.cookmeright.models.Recipe;
import com.example.aaronbrecher.cookmeright.models.Step;

import java.util.List;

public class RecipeDetailViewModel extends ViewModel {
    private List<Step> mSteps;
    private List<Ingredient> mIngredients;
    private Recipe mRecipe;

    public List<Step> getSteps() {
        return mSteps;
    }

    public void setSteps(List<Step> steps) {
        mSteps = steps;
    }

    public List<Ingredient> getIngredients() {
        return mIngredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
    }

    public Recipe getRecipe() {
        return mRecipe;
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }
}

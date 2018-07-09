package com.example.aaronbrecher.cookmeright.ViewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.aaronbrecher.cookmeright.models.Ingredient;
import com.example.aaronbrecher.cookmeright.models.Recipe;
import com.example.aaronbrecher.cookmeright.models.Step;

import java.util.List;

public class RecipeDetailViewModel extends ViewModel {
    private List<Step> mSteps;
    private MutableLiveData<Integer> mCurrentStepIndex;
    private List<Ingredient> mIngredients;
    private Recipe mRecipe;
    private String mRecipeName;
    private long mVideoPosition;
    private boolean mPlayWhenReady;
    private int mPreviousIndex = -1;

    public RecipeDetailViewModel() {
    }

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

    public int getPreviousIndex() {
        return mPreviousIndex;
    }

    public void setPreviousIndex(int previousIndex) {
        mPreviousIndex = previousIndex;
    }

    public String getRecipeName() {
        return mRecipeName;
    }

    public void setRecipeName(String recipeName) {
        mRecipeName = recipeName;
    }

    public long getVideoPosition() {
        return mVideoPosition;
    }

    public void setVideoPosition(long videoPosition) {
        mVideoPosition = videoPosition;
    }

    public boolean isPlayWhenReady() {
        return mPlayWhenReady;
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        mPlayWhenReady = playWhenReady;
    }

    public MutableLiveData<Integer> getCurrentStepIndex() {
        if(mCurrentStepIndex == null) mCurrentStepIndex = new MutableLiveData<>();
        return mCurrentStepIndex;
    }

    public void setCurrentStepIndex(Integer currentStepIndex) {
        if(mCurrentStepIndex == null) mCurrentStepIndex = new MutableLiveData<>();
        mCurrentStepIndex.postValue(currentStepIndex);
    }
}

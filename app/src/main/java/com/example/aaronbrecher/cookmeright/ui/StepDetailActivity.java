package com.example.aaronbrecher.cookmeright.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.aaronbrecher.cookmeright.R;
import com.example.aaronbrecher.cookmeright.ViewModels.RecipeDetailViewModel;
import com.example.aaronbrecher.cookmeright.models.Step;
import com.example.aaronbrecher.cookmeright.ui.fragments.MasterDetailFragment;

import java.util.ArrayList;
import java.util.List;

import static com.example.aaronbrecher.cookmeright.ui.RecipeDetailActivity.INTENT_EXTRA_RECIPE_NAME;
import static com.example.aaronbrecher.cookmeright.ui.RecipeDetailActivity.INTENT_EXTRA_STEP;
import static com.example.aaronbrecher.cookmeright.ui.RecipeDetailActivity.INTENT_EXTRA_STEP_LIST;

public class StepDetailActivity extends AppCompatActivity {

    public static final String FRAGMENT_ARGS_STEP = "step";
    public static final String FRAGMENT_ARGS_STEP_LIST = "steps length";
    public static final String FRAGMENT_ARGS_RECIPE_NAME = INTENT_EXTRA_RECIPE_NAME;
    private static final String RECIPE_DETAIL_TAG = "recipe detail tag";


    private RecipeDetailViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        mViewModel = ViewModelProviders.of(this).get(RecipeDetailViewModel.class);
        List<Step> steps = getIntent().getParcelableArrayListExtra(INTENT_EXTRA_STEP_LIST);
        mViewModel.setSteps(steps);
        int stepIndex = getIntent().getIntExtra(INTENT_EXTRA_STEP, -1);
        //only set the current index if this is the first time loading activity(i.e. don't set on orientation change)
        if(mViewModel.getCurrentStepIndex().getValue() == null) mViewModel.setCurrentStepIndex(stepIndex);
        mViewModel.setRecipeName(getIntent().getStringExtra(INTENT_EXTRA_RECIPE_NAME));
        if(mViewModel.getRecipeName() == null){
            Toast.makeText(this, "Must have recipe to show details :)", Toast.LENGTH_SHORT).show();
            finish();
        }
        mViewModel.getCurrentStepIndex().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                setTitle(mViewModel.getRecipeName() + " - Step " + (integer + 1));
            }
        });


        FragmentManager manager = getSupportFragmentManager();
        if(manager.findFragmentByTag(RECIPE_DETAIL_TAG) == null){
            manager.beginTransaction()
                    .add(R.id.master_detail_fragment_container, new MasterDetailFragment(), RECIPE_DETAIL_TAG)
                    .commit();
        }
    }
}

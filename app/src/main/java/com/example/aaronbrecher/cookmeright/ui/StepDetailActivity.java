package com.example.aaronbrecher.cookmeright.ui;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.aaronbrecher.cookmeright.R;
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

    private Step mStep;
    private List<Step> mSteps;
    private String mRecipeName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        mStep = getIntent().getParcelableExtra(INTENT_EXTRA_STEP);
        mSteps = getIntent().getParcelableArrayListExtra(INTENT_EXTRA_STEP_LIST);
        mRecipeName = getIntent().getStringExtra(INTENT_EXTRA_RECIPE_NAME);
        if(mStep == null || mRecipeName == null) finish();
        setTitle(mRecipeName + " - Step " + (mStep.getId()+1));

        FragmentManager manager = getSupportFragmentManager();
        if(manager.findFragmentByTag(RECIPE_DETAIL_TAG) == null){
            manager.beginTransaction()
                    .add(R.id.master_detail_fragment_container, getRecipeDetailMasterDetailFragment(), RECIPE_DETAIL_TAG)
                    .commit();
        }
    }

    /**
     * Function to create the detail fragment adding to it the correct step data
     * @return
     */
    @NonNull
    private MasterDetailFragment getRecipeDetailMasterDetailFragment() {
        //add the step data to the fragment as an argument
        Bundle bundle = new Bundle();
        bundle.putParcelable(FRAGMENT_ARGS_STEP, mStep);
        bundle.putParcelableArrayList(FRAGMENT_ARGS_STEP_LIST, new ArrayList<>(mSteps));
        bundle.putString(FRAGMENT_ARGS_RECIPE_NAME, mRecipeName);
        MasterDetailFragment detailFragment = new MasterDetailFragment();
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

}

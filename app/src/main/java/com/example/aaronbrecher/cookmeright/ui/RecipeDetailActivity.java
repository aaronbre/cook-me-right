package com.example.aaronbrecher.cookmeright.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.aaronbrecher.cookmeright.R;
import com.example.aaronbrecher.cookmeright.ViewModels.RecipeDetailViewModel;
import com.example.aaronbrecher.cookmeright.models.Recipe;
import com.example.aaronbrecher.cookmeright.models.Step;

import java.util.ArrayList;

import static com.example.aaronbrecher.cookmeright.ui.StepDetailActivity.FRAGMENT_ARGS_RECIPE_NAME;
import static com.example.aaronbrecher.cookmeright.ui.StepDetailActivity.FRAGMENT_ARGS_STEP;
import static com.example.aaronbrecher.cookmeright.ui.StepDetailActivity.FRAGMENT_ARGS_STEP_LIST;

public class RecipeDetailActivity extends AppCompatActivity implements ListItemClickListener {

    public static final String INTENT_EXTRA_RECIPE = "recipe";
    public static final String INTENT_EXTRA_STEP = "step";
    public static final String INTENT_EXTRA_RECIPE_NAME = "recipe name";
    public static final String INTENT_EXTRA_STEP_LIST = "step list";
    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    private static final String TAG_DETAIL_FRAGMENT = "detail fragment";
    private RecipeDetailViewModel mViewModel;

    //specific XML layouts where made for the tablet and phone, the tablet will show master list
    //layout while the phone will show a separate activity for each
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Recipe recipe = getIntent().getParcelableExtra(INTENT_EXTRA_RECIPE);
        //set up a viewModel this will be useful working with multiple fragments especially
        //for the tablet where everything will be in this activity
        mViewModel = ViewModelProviders.of(this).get(RecipeDetailViewModel.class);
        if (recipe != null) {
            setTitle(recipe.getName());
            mViewModel.setRecipe(recipe);
            mViewModel.setSteps(recipe.getSteps());
            mViewModel.setIngredients(recipe.getIngredients());
        }
        //set up the fragment, using this vs a static fragment because the viewModel was not
        //initialized when using a static fragment TODO look into this issue...
        RecipeDetailMasterListFragment masterListFragment = new RecipeDetailMasterListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.master_list_fragment_container, masterListFragment)
                .commit();

        //if this is tablet initialize the detail fragment to the first step
        if (getResources().getBoolean(R.bool.isTablet)) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(FRAGMENT_ARGS_STEP, mViewModel.getSteps().get(0));
            bundle.putParcelableArrayList(FRAGMENT_ARGS_STEP_LIST, new ArrayList<>(mViewModel.getSteps()));
            bundle.putString(FRAGMENT_ARGS_RECIPE_NAME, mViewModel.getRecipe().getName());
            RecipeDetailMasterDetailFragment detailFragment = new RecipeDetailMasterDetailFragment();
            detailFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.master_detail_fragment_container, detailFragment, TAG_DETAIL_FRAGMENT)
                    .commit();
        }
    }

    //depending on tablet or phone will either initialize a new activity or
    //a new fragment
    @Override
    public void onListItemClick(Parcelable data) {
        if (getResources().getBoolean(R.bool.isTablet)) {
            RecipeDetailMasterDetailFragment fragment = (RecipeDetailMasterDetailFragment) getSupportFragmentManager()
                    .findFragmentByTag(TAG_DETAIL_FRAGMENT);
            Step step = (Step) data;
            fragment.setStep(step);
            fragment.updateUiAndPlayer();
        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(INTENT_EXTRA_STEP, data);
            intent.putParcelableArrayListExtra(INTENT_EXTRA_STEP_LIST, new ArrayList<>(mViewModel.getSteps()));
            intent.putExtra(INTENT_EXTRA_RECIPE_NAME, mViewModel.getRecipe().getName());
            startActivity(intent);
        }
    }
}

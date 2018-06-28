package com.example.aaronbrecher.cookmeright.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.aaronbrecher.cookmeright.R;
import com.example.aaronbrecher.cookmeright.ViewModels.RecipeDetailViewModel;
import com.example.aaronbrecher.cookmeright.adapters.RecipeDetailPagerAdapter;
import com.example.aaronbrecher.cookmeright.models.Recipe;
import com.example.aaronbrecher.cookmeright.models.Step;
import com.example.aaronbrecher.cookmeright.ui.fragments.RecipeDetailMasterDetailFragment;
import com.example.aaronbrecher.cookmeright.ui.fragments.RecipeDetailMasterListFragment;

import java.util.ArrayList;

import static com.example.aaronbrecher.cookmeright.ui.StepDetailActivity.FRAGMENT_ARGS_RECIPE_NAME;
import static com.example.aaronbrecher.cookmeright.ui.StepDetailActivity.FRAGMENT_ARGS_STEP;
import static com.example.aaronbrecher.cookmeright.ui.StepDetailActivity.FRAGMENT_ARGS_STEP_LIST;

public class RecipeDetailActivity extends AppCompatActivity implements ListItemClickListener, TabLayout.OnTabSelectedListener {

    public static final String INTENT_EXTRA_RECIPE = "recipe";
    public static final String INTENT_EXTRA_STEP = "step";
    public static final String INTENT_EXTRA_RECIPE_NAME = "recipe name";
    public static final String INTENT_EXTRA_STEP_LIST = "step list";
    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    private static final String TAG_DETAIL_FRAGMENT = "detail fragment";

    private RecipeDetailViewModel mViewModel;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Fragment mDetailFragment;

    //specific XML layouts where made for the tablet and phone, the tablet will show master list
    //layout while the phone will show a separate activity for each
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.containsKey(TAG_DETAIL_FRAGMENT)){
            mDetailFragment = getSupportFragmentManager().getFragment(savedInstanceState, TAG_DETAIL_FRAGMENT);
        }
        setContentView(R.layout.activity_recipe_detail);

        //set up a viewModel this will be useful working with multiple fragments especially
        //for the tablet where everything will be in this activity
        Recipe recipe = getIntent().getParcelableExtra(INTENT_EXTRA_RECIPE);
        mViewModel = ViewModelProviders.of(this).get(RecipeDetailViewModel.class);
        if (recipe != null) {
            setTitle(recipe.getName());
            mViewModel.setRecipe(recipe);
            mViewModel.setSteps(recipe.getSteps());
            mViewModel.setIngredients(recipe.getIngredients());
        }

        //if the device is a phone will set up a tab layout for ingredients and steps
        RecipeDetailMasterListFragment masterListFragment = new RecipeDetailMasterListFragment();
        if (!getResources().getBoolean(R.bool.isTablet)) {

            mTabLayout = findViewById(R.id.recipe_detail_tab_layout);
            mViewPager = findViewById(R.id.recipe_detail_view_pager);

            mTabLayout.addTab(mTabLayout.newTab().setText("Ingredients"));
            mTabLayout.addTab(mTabLayout.newTab().setText("Recipe Steps"));
            mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            RecipeDetailPagerAdapter adapter = new RecipeDetailPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
            mViewPager.setAdapter(adapter);
            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

            mTabLayout.addOnTabSelectedListener(this);
        }

        //if device is a tablet no tablayout is needed, also will set up the stepDetailFragment
        //and initialize to the first step
        if (getResources().getBoolean(R.bool.isTablet)) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(FRAGMENT_ARGS_STEP, mViewModel.getSteps().get(0));
            bundle.putParcelableArrayList(FRAGMENT_ARGS_STEP_LIST, new ArrayList<>(mViewModel.getSteps()));
            bundle.putString(FRAGMENT_ARGS_RECIPE_NAME, mViewModel.getRecipe().getName());
            if(mDetailFragment == null){
                mDetailFragment = new RecipeDetailMasterDetailFragment();
                mDetailFragment.setArguments(bundle);
            }

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.master_detail_fragment_container, mDetailFragment, TAG_DETAIL_FRAGMENT)
                    .add(R.id.master_list_fragment_container, masterListFragment)
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mDetailFragment != null){
            getSupportFragmentManager().putFragment(outState, TAG_DETAIL_FRAGMENT, mDetailFragment);
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}

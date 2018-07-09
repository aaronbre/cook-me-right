package com.example.aaronbrecher.cookmeright.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.aaronbrecher.cookmeright.R;
import com.example.aaronbrecher.cookmeright.ViewModels.RecipeDetailViewModel;
import com.example.aaronbrecher.cookmeright.adapters.RecipeDetailPagerAdapter;
import com.example.aaronbrecher.cookmeright.models.Recipe;
import com.example.aaronbrecher.cookmeright.models.Step;
import com.example.aaronbrecher.cookmeright.ui.fragments.MasterDetailFragment;
import com.example.aaronbrecher.cookmeright.ui.fragments.MasterListFragment;
import com.example.aaronbrecher.cookmeright.utils.PrefsUtils;
import com.example.aaronbrecher.cookmeright.widget.RecipeWidgetService;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity implements ListItemClickListener, TabLayout.OnTabSelectedListener {

    public static final String INTENT_EXTRA_RECIPE = "recipe";
    public static final String INTENT_EXTRA_STEP = "step";
    public static final String INTENT_EXTRA_RECIPE_NAME = "recipe name";
    public static final String INTENT_EXTRA_STEP_LIST = "step list";
    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    private static final String TAG_DETAIL_FRAGMENT = "detail fragment";
    private static final String TAG_LIST_FRAGMENT = "list fragment";

    private RecipeDetailViewModel mViewModel;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Fragment mDetailFragment;

    //specific XML layouts where made for the tablet and phone, the tablet will show master list
    //layout while the phone will show a separate activity for each
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            mViewModel.setRecipeName(recipe.getName());
            //only set the current index if this is the first time loading activity(i.e. don't set on orientation change)
            if(mViewModel.getCurrentStepIndex().getValue() == null) mViewModel.setCurrentStepIndex(0);
        }

        //if the device is a phone will set up a tab layout for ingredients and steps
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

        // if device is a tablet no tablayout is needed, also will set up the stepDetailFragment
        // and initialize to the first step, will only create a new Fragment if there is not one
        // already
        if (getResources().getBoolean(R.bool.isTablet)) {
            mViewModel.getCurrentStepIndex().observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(@Nullable Integer integer) {
                    setTitle(mViewModel.getRecipeName() + " - Step " + (integer+1));
                }
            });

            FragmentManager manager = getSupportFragmentManager();
            if(manager.findFragmentByTag(TAG_DETAIL_FRAGMENT ) == null){
                mDetailFragment = new MasterDetailFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.master_detail_fragment_container, mDetailFragment, TAG_DETAIL_FRAGMENT)
                        .commit();
            }
            if(getSupportFragmentManager().findFragmentByTag(TAG_LIST_FRAGMENT) == null){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.master_list_fragment_container, new MasterListFragment(), TAG_LIST_FRAGMENT)
                        .commit();
            }

        }
    }

    //depending on tablet or phone will either initialize a new activity or
    //a new fragment
    @Override
    public void onListItemClick(Parcelable data) {
        if (getResources().getBoolean(R.bool.isTablet)) {
            Step step = (Step) data;
            mViewModel.setCurrentStepIndex(step.getId());
        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            Step step = (Step) data;
            intent.putExtra(INTENT_EXTRA_STEP, step.getId());
            intent.putParcelableArrayListExtra(INTENT_EXTRA_STEP_LIST, new ArrayList<>(mViewModel.getSteps()));
            intent.putExtra(INTENT_EXTRA_RECIPE_NAME, mViewModel.getRecipe().getName());
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_with_add_to_widget_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_add_to_widget) {
            PrefsUtils.addRecipeToPrefs(mViewModel.getRecipe(), PreferenceManager.getDefaultSharedPreferences(this));
            Toast.makeText(this, R.string.added_to_widget_toast, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, RecipeWidgetService.class);
            intent.setAction(RecipeWidgetService.ACTION_UPDATE_RECIPE_WIDGET);
            this.startService(intent);
        }
        return super.onOptionsItemSelected(item);
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

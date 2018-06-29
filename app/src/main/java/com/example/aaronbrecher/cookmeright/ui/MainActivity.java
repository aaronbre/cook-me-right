package com.example.aaronbrecher.cookmeright.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.aaronbrecher.cookmeright.R;
import com.example.aaronbrecher.cookmeright.adapters.RecipeListAdapter;
import com.example.aaronbrecher.cookmeright.models.Recipe;
import com.example.aaronbrecher.cookmeright.network.RecipeApiUtils;
import com.example.aaronbrecher.cookmeright.network.RecipeService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ListItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    //TODO only for testing for production change INTENT_EXTRA to private and remove test recipe
    public static final String INTENT_EXTRA_RECIPE = "recipe";
    public Recipe testRecipe;
    private RecyclerView mRecyclerView;
    private ImageView mProgress;
    private RecipeService mRecipeService = RecipeApiUtils.createService();
    private RecipeListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgress = findViewById(R.id.recipe_list_loading_graphic);
        mRecyclerView = findViewById(R.id.main_activity_recipe_list_rv);
        mAdapter = new RecipeListAdapter(null, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, getNumColumnsForDevice()));
        showLoadingGraphic();
        loadData();
    }


    //load the data from the server using retrofit to request and store the data in
    //model classes
    private void loadData() {
        mRecipeService.queryRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                List<Recipe> recipes = response.body();
                testRecipe = recipes.get(0);
                mAdapter.swapLists(recipes);
                hideLoadingAndShowList();
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure: Failed to load data " + t.getLocalizedMessage());
            }
        });
    }

    // show a animated loading graphic while the network request is made
    private void showLoadingGraphic() {
        mProgress.setVisibility(View.VISIBLE);
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        mProgress.startAnimation(rotate);
        mRecyclerView.setVisibility(View.GONE);
    }


    private void hideLoadingAndShowList() {
        mProgress.clearAnimation();
        mProgress.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    //configure amount of columns for the grid depending on tablet vs phone and
    //portrait vs landscape
    private int getNumColumnsForDevice() {
        int spanCount;
        if(getResources().getBoolean(R.bool.isTablet)){
            spanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2;
        }else{
            spanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1;
        }
        return spanCount;
    }

    // Implemented interface to launch the recipe detail activity from the recycler view
    // the method will pass the recipe object to the activity
    @Override
    public void onListItemClick(Parcelable recipe) {
        Intent intent = new Intent(this,RecipeDetailActivity.class);
        intent.putExtra(INTENT_EXTRA_RECIPE, recipe);
        startActivity(intent);
    }
}

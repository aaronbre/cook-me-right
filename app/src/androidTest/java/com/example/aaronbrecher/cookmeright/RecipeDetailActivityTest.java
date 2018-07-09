package com.example.aaronbrecher.cookmeright;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.aaronbrecher.cookmeright.adapters.StepListAdapter;
import com.example.aaronbrecher.cookmeright.models.Recipe;
import com.example.aaronbrecher.cookmeright.ui.RecipeDetailActivity;
import com.example.aaronbrecher.cookmeright.utils.UiUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {

    private static final String TAG = RecipeDetailActivityTest.class.getSimpleName();
    private Recipe mRecipe;
    @Rule
    public IntentsTestRule<RecipeDetailActivity> mIntentsTestRule =
            new IntentsTestRule<>(RecipeDetailActivity.class, false, false);

    @Before
    public void setUpWithIntent(){
        mRecipe = Recipe.getMockRecipe();
        Intent intent = new Intent();
        intent.putExtra("recipe", mRecipe);
        mIntentsTestRule.launchActivity(intent);
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void ingredientsIsProperlyFormattedAndDisplayed() {
        onView(withId(R.id.master_list_ingredients)).check(matches(withText(UiUtils.setUpIngredientsList(mRecipe.getIngredients()))));
    }

    //check that the viewPager works, if it did not recyclerView will not be on the screen
    //and test will fail
    @Test
    public void viewPagerMovesToStepList(){
        onView(withId(R.id.recipe_detail_view_pager)).perform(swipeLeft());
        onView(withId(R.id.master_list_step_list)).perform(RecyclerViewActions
                .<StepListAdapter.StepListViewHolder>scrollToPosition(0));
        onView(withId(R.id.master_list_step_list)).perform(RecyclerViewActions
                .<StepListAdapter.StepListViewHolder>actionOnItemAtPosition(0, click()));
    }

}

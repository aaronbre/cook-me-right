package com.example.aaronbrecher.cookmeright;

import android.content.Context;
import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.aaronbrecher.cookmeright.adapters.StepListAdapter;
import com.example.aaronbrecher.cookmeright.models.Recipe;
import com.example.aaronbrecher.cookmeright.ui.MainActivity;
import com.example.aaronbrecher.cookmeright.ui.RecipeDetailActivity;
import com.example.aaronbrecher.cookmeright.utils.UiUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {

    private static final String TAG = RecipeDetailActivityTest.class.getSimpleName();
    private Recipe mRecipe;
    @Rule
    public IntentsTestRule<RecipeDetailActivity> mIntentsTestRule =
            new IntentsTestRule<>(RecipeDetailActivity.class, false, false);

    @Before
    public void setUpWithIntent(){
        Context context = getInstrumentation().getTargetContext();
        mRecipe = Recipe.getMockRecipe(context);
        Intent intent = new Intent();
        intent.putExtra("recipe", mRecipe);
        mIntentsTestRule.launchActivity(intent);
    }

    @Test
    public void ingredientsIsProperlyFormattedAndDisplayed() {
        onView(withId(R.id.master_list_ingredients)).check(matches(withText(UiUtils.setUpIngredientsList(mRecipe.getIngredients()))));
    }

    @Test
    public void viewPagerMovesToStepList(){
        onView(withId(R.id.recipe_detail_view_pager)).perform(swipeLeft());
        onView(withId(R.id.master_list_step_list)).perform(RecyclerViewActions.<StepListAdapter.StepListViewHolder>scrollToPosition(0));
        onView(withId(R.id.step_list_item_number)).check(matches(withText("1")));
        onView(withId(R.id.step_list_item_description)).check(matches(withText(mRecipe.getSteps().get(0).getDescription())));
        onView(withId(R.id.master_list_step_list)).perform(RecyclerViewActions
                .<StepListAdapter.StepListViewHolder>actionOnItemAtPosition(0, click()));
        intended(allOf(hasExtra(RecipeDetailActivity.INTENT_EXTRA_STEP, mRecipe.getSteps().get(0))));

    }

}

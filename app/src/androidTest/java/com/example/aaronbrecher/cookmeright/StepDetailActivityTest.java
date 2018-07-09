package com.example.aaronbrecher.cookmeright;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.aaronbrecher.cookmeright.models.Recipe;
import com.example.aaronbrecher.cookmeright.models.Step;
import com.example.aaronbrecher.cookmeright.ui.StepDetailActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.aaronbrecher.cookmeright.ui.RecipeDetailActivity.INTENT_EXTRA_RECIPE_NAME;
import static com.example.aaronbrecher.cookmeright.ui.RecipeDetailActivity.INTENT_EXTRA_STEP;
import static com.example.aaronbrecher.cookmeright.ui.RecipeDetailActivity.INTENT_EXTRA_STEP_LIST;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class StepDetailActivityTest {

    private Recipe mRecipe;
    private Step mStep;

    @Rule
    public IntentsTestRule<StepDetailActivity> mIntentsTestRule =
            new IntentsTestRule<>(StepDetailActivity.class, false, false);

    @Before
    public void setUpActivityWithIntent(){
        mRecipe = Recipe.getMockRecipe();
        mStep = mRecipe.getSteps().get(0);
        Intent intent = new Intent();
        intent.putExtra(INTENT_EXTRA_STEP, mStep.getId());
        intent.putParcelableArrayListExtra(INTENT_EXTRA_STEP_LIST, new ArrayList<>(mRecipe.getSteps()));
        intent.putExtra(INTENT_EXTRA_RECIPE_NAME, mRecipe.getName());
        mIntentsTestRule.launchActivity(intent);
    }

    @Test
    public void assertTextViewsAreCorrect(){
        onView(withId(R.id.step_detail_instructions)).check(matches(withText(mStep.getDescription())));
        mIntentsTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if(mIntentsTestRule.getActivity().getResources().getBoolean(R.bool.isTablet)){
            onView(withId(R.id.step_detail_next)).check(matches(isDisplayed()));
            onView(withId(R.id.step_detail_previous)).check(matches(isDisplayed()));
        }else {
            onView(withId(R.id.step_detail_next)).check(doesNotExist());
            onView(withId(R.id.step_detail_previous)).check(doesNotExist());
        }
    }
}

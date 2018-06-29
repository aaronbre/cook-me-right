package com.example.aaronbrecher.cookmeright;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.aaronbrecher.cookmeright.adapters.RecipeListAdapter;
import com.example.aaronbrecher.cookmeright.models.Recipe;
import com.example.aaronbrecher.cookmeright.ui.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> mActivityTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Before
    public void stubExternalIntents(){
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void ClickOnRecipeSendsIntent(){
        onView(withId(R.id.main_activity_recipe_list_rv)).perform(RecyclerViewActions.<RecipeListAdapter
                .RecipeListViewHolder>actionOnItemAtPosition(0, click()));
        Recipe recipe = new Recipe();

        intended(allOf(hasExtra(MainActivity.INTENT_EXTRA_RECIPE, mActivityTestRule.getActivity().testRecipe)));
    }
}

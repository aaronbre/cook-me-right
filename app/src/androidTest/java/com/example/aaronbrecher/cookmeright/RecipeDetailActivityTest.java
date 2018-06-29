package com.example.aaronbrecher.cookmeright;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.aaronbrecher.cookmeright.models.Recipe;
import com.example.aaronbrecher.cookmeright.ui.RecipeDetailActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {

    private Recipe mRecipe;
    @Rule
    public IntentsTestRule<RecipeDetailActivity> mIntentsTestRule =
            new IntentsTestRule<>(RecipeDetailActivity.class, false, false);

    @Before
    public void setUp(){
        mRecipe = mock(Recipe.class, RETURNS_DEEP_STUBS);
    }

    @Test
    public void RecipeActivityWorksWithIntentData() {
        Intent intent = new Intent();
        intent.putExtra("recipe", mRecipe);
        mIntentsTestRule.launchActivity(intent);
    }

}

package com.example.aaronbrecher.cookmeright.utils;

import android.content.SharedPreferences;

import com.example.aaronbrecher.cookmeright.models.Recipe;
public class PrefsUtils {
    public static final String PREFS_RECIPE_KEY = "recipe";

    public PrefsUtils() {
    }

    public static void addRecipeToPrefs(Recipe recipe, SharedPreferences preferences){
        String recipeJson = Recipe.convertToJsonString(recipe);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFS_RECIPE_KEY, recipeJson);
        editor.apply();
    }

    public static Recipe getRecipeFromPrefs(SharedPreferences preferences){
        String recipeJson = preferences.getString(PREFS_RECIPE_KEY, "");
        return Recipe.convertFromJson(recipeJson);
    }
}

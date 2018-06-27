package com.example.aaronbrecher.cookmeright.network;

import com.example.aaronbrecher.cookmeright.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeService {
    @GET(RecipeApiUtils.RECIPE_LIST_ENDPOINT)
    Call<List<Recipe>> queryRecipes();
}

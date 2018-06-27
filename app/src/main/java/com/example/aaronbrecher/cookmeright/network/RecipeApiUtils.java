package com.example.aaronbrecher.cookmeright.network;

public class RecipeApiUtils {
    public static final String RECIPE_LIST_ENDPOINT = "topher/2017/May/59121517_baking/baking.json";
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    public static RecipeService createService() {
        return RetrofitClient.getClient(BASE_URL).create(RecipeService.class);
    }
}

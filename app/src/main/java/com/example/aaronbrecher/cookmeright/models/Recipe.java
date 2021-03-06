package com.example.aaronbrecher.cookmeright.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.aaronbrecher.cookmeright.R;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ingredients")
    @Expose
    private List<Ingredient> ingredients = null;
    @SerializedName("steps")
    @Expose
    private List<Step> steps = null;
    @SerializedName("servings")
    @Expose
    private Integer servings;
    @SerializedName("image")
    @Expose
    private String image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Recipe() {
    }

    public static String convertToJsonString(Recipe recipe){
        Gson gson = new Gson();
        return gson.toJson(recipe);
    }

    public static Recipe convertFromJson(String json){
        Gson gson = new Gson();
        if(json.isEmpty()) return null;
        return gson.fromJson(json, Recipe.class);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeList(this.ingredients);
        dest.writeList(this.steps);
        dest.writeValue(this.servings);
        dest.writeString(this.image);
    }

    protected Recipe(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.ingredients = new ArrayList<Ingredient>();
        in.readList(this.ingredients, Ingredient.class.getClassLoader());
        this.steps = new ArrayList<Step>();
        in.readList(this.steps, Step.class.getClassLoader());
        this.servings = (Integer) in.readValue(Integer.class.getClassLoader());
        this.image = in.readString();
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };


    //TODO remove after testing...
    public static Recipe getMockRecipe(Context context){
        Gson gson = new Gson();
        String json = "{\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"Nutella Pie\",\n" +
                "    \"ingredients\": [\n" +
                "      {\n" +
                "        \"quantity\": 2,\n" +
                "        \"measure\": \"CUP\",\n" +
                "        \"ingredient\": \"Graham Cracker crumbs\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"quantity\": 6,\n" +
                "        \"measure\": \"TBLSP\",\n" +
                "        \"ingredient\": \"unsalted butter, melted\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"quantity\": 0.5,\n" +
                "        \"measure\": \"CUP\",\n" +
                "        \"ingredient\": \"granulated sugar\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"quantity\": 1.5,\n" +
                "        \"measure\": \"TSP\",\n" +
                "        \"ingredient\": \"salt\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"quantity\": 5,\n" +
                "        \"measure\": \"TBLSP\",\n" +
                "        \"ingredient\": \"vanilla\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"quantity\": 1,\n" +
                "        \"measure\": \"K\",\n" +
                "        \"ingredient\": \"Nutella or other chocolate-hazelnut spread\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"quantity\": 500,\n" +
                "        \"measure\": \"G\",\n" +
                "        \"ingredient\": \"Mascapone Cheese(room temperature)\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"quantity\": 1,\n" +
                "        \"measure\": \"CUP\",\n" +
                "        \"ingredient\": \"heavy cream(cold)\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"quantity\": 4,\n" +
                "        \"measure\": \"OZ\",\n" +
                "        \"ingredient\": \"cream cheese(softened)\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"steps\": [\n" +
                "      {\n" +
                "        \"id\": 0,\n" +
                "        \"shortDescription\": \"Recipe Introduction\",\n" +
                "        \"description\": \"Recipe Introduction\",\n" +
                "        \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4\",\n" +
                "        \"thumbnailURL\": \"\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 1,\n" +
                "        \"shortDescription\": \"Starting prep\",\n" +
                "        \"description\": \"1. Preheat the oven to 350\\u00b0F. Butter a 9\\\" deep dish pie pan.\",\n" +
                "        \"videoURL\": \"\",\n" +
                "        \"thumbnailURL\": \"\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 2,\n" +
                "        \"shortDescription\": \"Prep the cookie crust.\",\n" +
                "        \"description\": \"2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.\",\n" +
                "        \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9a6_2-mix-sugar-crackers-creampie/2-mix-sugar-crackers-creampie.mp4\",\n" +
                "        \"thumbnailURL\": \"\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3,\n" +
                "        \"shortDescription\": \"Press the crust into baking form.\",\n" +
                "        \"description\": \"3. Press the cookie crumb mixture into the prepared pie pan and bake for 12 minutes. Let crust cool to room temperature.\",\n" +
                "        \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9cb_4-press-crumbs-in-pie-plate-creampie/4-press-crumbs-in-pie-plate-creampie.mp4\",\n" +
                "        \"thumbnailURL\": \"\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 4,\n" +
                "        \"shortDescription\": \"Start filling prep\",\n" +
                "        \"description\": \"4. Beat together the nutella, mascarpone, 1 teaspoon of salt, and 1 tablespoon of vanilla on medium speed in a stand mixer or high speed with a hand mixer until fluffy.\",\n" +
                "        \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd97a_1-mix-marscapone-nutella-creampie/1-mix-marscapone-nutella-creampie.mp4\",\n" +
                "        \"thumbnailURL\": \"\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 5,\n" +
                "        \"shortDescription\": \"Finish filling prep\",\n" +
                "        \"description\": \"5. Beat the cream cheese and 50 grams (1/4 cup) of sugar on medium speed in a stand mixer or high speed with a hand mixer for 3 minutes. Decrease the speed to medium-low and gradually add in the cold cream. Add in 2 teaspoons of vanilla and beat until stiff peaks form.\",\n" +
                "        \"videoURL\": \"\",\n" +
                "        \"thumbnailURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda20_7-add-cream-mix-creampie/7-add-cream-mix-creampie.mp4\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 6,\n" +
                "        \"shortDescription\": \"Finishing Steps\",\n" +
                "        \"description\": \"6. Pour the filling into the prepared crust and smooth the top. Spread the whipped cream over the filling. Refrigerate the pie for at least 2 hours. Then it's ready to serve!\",\n" +
                "        \"videoURL\": \"https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda45_9-add-mixed-nutella-to-crust-creampie/9-add-mixed-nutella-to-crust-creampie.mp4\",\n" +
                "        \"thumbnailURL\": \"\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"servings\": 8,\n" +
                "    \"image\": \"\"\n" +
                "  }";
        return gson.fromJson(json, Recipe.class);
    }

}
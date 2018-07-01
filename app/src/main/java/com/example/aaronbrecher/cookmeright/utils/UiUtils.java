package com.example.aaronbrecher.cookmeright.utils;

import android.widget.TextView;

import com.example.aaronbrecher.cookmeright.models.Ingredient;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class UiUtils {
    public static String setUpIngredientsList(List<Ingredient> ingredients){
        StringBuilder builder = new StringBuilder();
        DecimalFormat dm = new DecimalFormat("0.#");
        for(Ingredient ingredient : ingredients){
            String line = String.format(Locale.getDefault(),
                    dm.format(ingredient.getQuantity()) + " %s %s \n",ingredient.getMeasure(), ingredient.getIngredient());
            builder.append(line);
        }
        return builder.toString();
    }
}

package com.example.aaronbrecher.cookmeright.ui.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aaronbrecher.cookmeright.R;
import com.example.aaronbrecher.cookmeright.ViewModels.RecipeDetailViewModel;
import com.example.aaronbrecher.cookmeright.models.Ingredient;
import com.example.aaronbrecher.cookmeright.ui.RecipeDetailActivity;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

/**
 * Fragment to be used when utilizing a tab layout on a phone
 */
public class IngredientsTabLayoutFragment extends Fragment {
    private RecipeDetailViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredients_tab, container, false);
        RecipeDetailActivity activity = (RecipeDetailActivity) getActivity();
        mViewModel = ViewModelProviders.of(activity).get(RecipeDetailViewModel.class);
        setUpIngredients((TextView) rootView.findViewById(R.id.master_list_ingredients));
        return rootView;
    }

    private void setUpIngredients(TextView textView) {
        List<Ingredient> ingredients = mViewModel.getIngredients();
        StringBuilder builder = new StringBuilder();
        DecimalFormat dm = new DecimalFormat("0.#");
        for(Ingredient ingredient : ingredients){
            String line = String.format(Locale.getDefault(),
                    dm.format(ingredient.getQuantity()) + " %s %s \n",ingredient.getMeasure(), ingredient.getIngredient());
            builder.append(line);
        }
        textView.setText(builder.toString());
    }
}

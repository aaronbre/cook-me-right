package com.example.aaronbrecher.cookmeright.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aaronbrecher.cookmeright.R;
import com.example.aaronbrecher.cookmeright.ViewModels.RecipeDetailViewModel;
import com.example.aaronbrecher.cookmeright.models.Ingredient;
import com.example.aaronbrecher.cookmeright.models.Step;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class RecipeDetailMasterListFragment extends Fragment {
    private StepListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ListItemClickListener mClickListener;
    private RecipeDetailActivity mActivity;
    private RecipeDetailViewModel mViewModel;

    public RecipeDetailMasterListFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mClickListener = (ListItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ListItemClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);
        mActivity = (RecipeDetailActivity) getActivity();
        mViewModel = ViewModelProviders.of(mActivity).get(RecipeDetailViewModel.class);
        mRecyclerView = rootView.findViewById(R.id.master_list_step_list);
        Log.d(TAG, "onCreateView: " + mViewModel.getSteps());
        mAdapter = new StepListAdapter(mViewModel.getSteps(), mClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
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

package com.example.aaronbrecher.cookmeright.ui.fragments;

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
import com.example.aaronbrecher.cookmeright.adapters.StepListAdapter;
import com.example.aaronbrecher.cookmeright.models.Ingredient;
import com.example.aaronbrecher.cookmeright.ui.ListItemClickListener;
import com.example.aaronbrecher.cookmeright.ui.RecipeDetailActivity;
import com.example.aaronbrecher.cookmeright.utils.UiUtils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class MasterListFragment extends Fragment {
    private StepListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ListItemClickListener mClickListener;
    private RecipeDetailActivity mActivity;
    private RecipeDetailViewModel mViewModel;

    public MasterListFragment() {

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
        //if the device is a tablet need to set up the ingredients here. on a phone
        //ingredients are in a seperate tab/fragment
        if(getResources().getBoolean(R.bool.isTablet)){
            TextView textView = rootView.findViewById(R.id.master_list_ingredients);
            textView.setText(UiUtils.setUpIngredientsList(mViewModel.getIngredients()));
        }
        return rootView;

    }
}

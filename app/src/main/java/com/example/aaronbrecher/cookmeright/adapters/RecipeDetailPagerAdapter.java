package com.example.aaronbrecher.cookmeright.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.aaronbrecher.cookmeright.ui.fragments.IngredientsTabLayoutFragment;
import com.example.aaronbrecher.cookmeright.ui.fragments.MasterListFragment;

public class RecipeDetailPagerAdapter extends FragmentStatePagerAdapter {

    private final int numTabs;

    public RecipeDetailPagerAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.numTabs = numTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new IngredientsTabLayoutFragment();
            case 1:
                return new MasterListFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}

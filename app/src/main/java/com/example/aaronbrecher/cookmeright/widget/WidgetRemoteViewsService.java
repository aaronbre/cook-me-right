package com.example.aaronbrecher.cookmeright.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.aaronbrecher.cookmeright.R;
import com.example.aaronbrecher.cookmeright.models.Ingredient;
import com.example.aaronbrecher.cookmeright.models.Recipe;
import com.example.aaronbrecher.cookmeright.utils.PrefsUtils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class WidgetRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetListRemoteViewsFactory(this.getApplicationContext());
    }
}

class WidgetListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private List<Ingredient> mIngredients;

    WidgetListRemoteViewsFactory(Context appContext) {
        mContext = appContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        Recipe recipe = PrefsUtils.getRecipeFromPrefs(sharedPreferences);
        mIngredients = recipe.getIngredients();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mIngredients == null ? 0 : mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = mIngredients.get(position);
        DecimalFormat dm = new DecimalFormat("0.#");
        String ingredientString = String.format(Locale.getDefault(),
                dm.format(ingredient.getQuantity()) + " %s %s \n",ingredient.getMeasure(), ingredient.getIngredient());
        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        view.setTextViewText(R.id.widget_list_item, ingredientString);
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

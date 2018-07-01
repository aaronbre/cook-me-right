package com.example.aaronbrecher.cookmeright.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.RemoteViewsService;

import com.example.aaronbrecher.cookmeright.models.Recipe;
import com.example.aaronbrecher.cookmeright.utils.PrefsUtils;

public class RecipeWidgetService extends IntentService {


    public static final String ACTION_UPDATE_RECIPE_WIDGET = "com.example.aaronbrecher.cookmeright.action.update_widgets";

    public RecipeWidgetService() {
        super("RecipeService");
    }

    public static void startActionUpdateWidget(Context context){
        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGET);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            if(intent.getAction().equals(ACTION_UPDATE_RECIPE_WIDGET)){
                handleActionUpdateWidgets();
            }
        }
    }

    private void handleActionUpdateWidgets() {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        int [] widgetIds = widgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        Recipe recipe = PrefsUtils.getRecipeFromPrefs(PreferenceManager.getDefaultSharedPreferences(this));
        RecipeWidgetProvider.updateRecipeWidgets(this, widgetManager, widgetIds, recipe);
    }
}

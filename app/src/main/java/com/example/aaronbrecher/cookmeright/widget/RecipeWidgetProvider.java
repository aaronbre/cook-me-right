package com.example.aaronbrecher.cookmeright.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.aaronbrecher.cookmeright.R;
import com.example.aaronbrecher.cookmeright.models.Recipe;
import com.example.aaronbrecher.cookmeright.ui.MainActivity;
import com.example.aaronbrecher.cookmeright.ui.RecipeDetailActivity;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe recipe) {
        RemoteViews views = getRemoteViews(context, recipe);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static void updateRecipeWidgets(Context context, AppWidgetManager widgetManager, int[] widgetIds, Recipe recipe) {
        for (int widgetId : widgetIds){
            updateAppWidget(context, widgetManager, widgetId, recipe);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RecipeWidgetService.startActionUpdateWidget(context);
    }

    private static RemoteViews getRemoteViews(Context context, Recipe recipe){
        Intent intent;
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.recipe_app_widget);
        if(recipe == null){
            intent = new Intent(context, MainActivity.class);
        }else {
            intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.INTENT_EXTRA_RECIPE, recipe);
            remoteViews.setTextViewText(R.id.appwidget_text_heading, recipe.getName());
            Intent adapterIntent = new Intent(context, WidgetRemoteViewsService.class);
            remoteViews.setRemoteAdapter(R.id.appwidget_text_ingredients_list, adapterIntent);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.appwidget_container, pendingIntent);
        remoteViews.setPendingIntentTemplate(R.id.appwidget_text_ingredients_list, pendingIntent);
        return remoteViews;
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


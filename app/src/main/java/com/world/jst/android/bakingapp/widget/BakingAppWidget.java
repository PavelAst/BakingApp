package com.world.jst.android.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.world.jst.android.bakingapp.R;
import com.world.jst.android.bakingapp.activity.RecipeDetailsActivity;
import com.world.jst.android.bakingapp.activity.RecipesListActivity;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int recipeId, String recipeName) {

        PendingIntent pendingIntent;
        if (recipeId < 1 || recipeName == null) {
            Intent intent = new Intent(context, RecipesListActivity.class);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        } else {
            // Create an Intent to launch RecipeDetailsActivity when clicked
            Bundle bundle = new Bundle();
            bundle.putInt(RecipeDetailsActivity.RECIPE_ITEM_ID, recipeId);
            bundle.putString(RecipeDetailsActivity.RECIPE_ITEM_NAME, recipeName);

            Intent intent = new Intent(context, RecipeDetailsActivity.class);
            intent.putExtras(bundle);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, FLAG_UPDATE_CURRENT);
        }

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        if (recipeName != null && !recipeName.isEmpty()) {
            views.setTextViewText(R.id.widget_recipe_name_tv, recipeName);
        }

        // Widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAllAppWidget(Context context, AppWidgetManager appWidgetManager,
                                          int[] appWidgetIds, int recipeId, String recipeName) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeId, recipeName);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        BakingAppRecipeService.startActionUpdatePlantWidgets(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        BakingAppRecipeService.startActionUpdatePlantWidgets(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }
}


package com.world.jst.android.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.world.jst.android.bakingapp.R;
import com.world.jst.android.bakingapp.activity.RecipeDetailsActivity;
import com.world.jst.android.bakingapp.model.IngredientParcelable;

import java.util.ArrayList;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    private static final String RECIPE_ITEM_ID = "recipe_item_id";
    private static final String RECIPE_ITEM_NAME = "recipe_item_name";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int recipeId, String recipeName,
                                ArrayList<IngredientParcelable> ingredientParcelables) {

        Log.d("Widgets", "### In BakingAppWidget - updateAppWidget");

//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
//        if (recipeName != null && !recipeName.isEmpty()) {
//            views.setTextViewText(R.id.widget_recipe_name_tv, recipeName);
//        }
//        // Set the ListViewWidgetService intent to act as the adapter for the ListView
//        Intent intent = new Intent(context, ListViewWidgetService.class);
//        intent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, ingredientParcelables);
//        views.setRemoteAdapter(R.id.widget_recipe_ingredients_lw, intent);
//
//        // Create an Intent to launch RecipeDetailsActivity when clicked
//        Bundle bundle = new Bundle();
//        bundle.putInt(RECIPE_ITEM_ID, recipeId);
//        bundle.putString(RECIPE_ITEM_NAME, recipeName);
//
//        Intent appIntent = new Intent(context, RecipeDetailsActivity.class);
//        appIntent.putExtras(bundle);
//        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, FLAG_UPDATE_CURRENT);
//        // Widgets allow click handlers to only launch pending intents
//        views.setOnClickPendingIntent(R.id.widget_layout, appPendingIntent);

        // Simple solution
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget2);
        if (recipeName != null && !recipeName.isEmpty()) {
            views.setTextViewText(R.id.widget_recipe_name_tv, recipeName);
        }
        if (ingredientParcelables != null && !ingredientParcelables.isEmpty()) {
            Log.d("Widgets", "### In BakingAppWidget - updateAppWidget - ingredientParcelables: " + ingredientParcelables.size());
            StringBuilder ingredients = new StringBuilder();
            for (IngredientParcelable ingredient : ingredientParcelables) {
                ingredients.append(ingredient.toString());
            }
            views.setTextViewText(R.id.widget_all_ingredients_tv, ingredients.toString());
        }

        // Create an Intent to launch RecipeDetailsActivity when clicked
        Bundle bundle = new Bundle();
        bundle.putInt(RECIPE_ITEM_ID, recipeId);
        bundle.putString(RECIPE_ITEM_NAME, recipeName);

        Intent appIntent = new Intent(context, RecipeDetailsActivity.class);
        appIntent.putExtras(bundle);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, FLAG_UPDATE_CURRENT);

        // Widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.widget_layout, appPendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAllAppWidget(Context context, AppWidgetManager appWidgetManager,
                                          int[] appWidgetIds, int recipeId, String recipeName,
                                          ArrayList<IngredientParcelable> ingredientParcelables) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeId, recipeName,
                    ingredientParcelables);
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


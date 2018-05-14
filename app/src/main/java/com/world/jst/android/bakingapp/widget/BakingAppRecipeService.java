package com.world.jst.android.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.world.jst.android.bakingapp.utils.CurrentRecipeData;

public class BakingAppRecipeService extends IntentService {

    public static final String ACTION_UPDATE_RECIPE_WIDGETS = "com.world.jst.android.bakingapp.widget.action.update_recipe_widgets";

    public BakingAppRecipeService() {
        super("BakingAppRecipeService");
    }

    //    public static void startActionUpdatePlantWidgets(Context context, int recipeId, String recipeName) {
    public static void startActionUpdatePlantWidgets(Context context) {
        Intent intent = new Intent(context, BakingAppRecipeService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGETS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_RECIPE_WIDGETS.equals(action)) {
                handleActionUpdateAppWidgets();
            }
        }
    }

    //    private void handleActionUpdateAppWidgets(int recipeId, String recipeName) {
    private void handleActionUpdateAppWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));

        int recipeId = CurrentRecipeData.getRecipeId(this);
        String recipeName = CurrentRecipeData.getRecipeName(this);

        BakingAppWidget.updateAllAppWidget(this, appWidgetManager, appWidgetIds, recipeId, recipeName);
    }
}

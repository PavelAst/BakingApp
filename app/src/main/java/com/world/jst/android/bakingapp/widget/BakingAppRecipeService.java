package com.world.jst.android.bakingapp.widget;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.world.jst.android.bakingapp.model.Ingredient;
import com.world.jst.android.bakingapp.model.IngredientParcelable;
import com.world.jst.android.bakingapp.model.Recipe;
import com.world.jst.android.bakingapp.utils.CurrentRecipeData;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;

public class BakingAppRecipeService extends IntentService {

    public static final String ACTION_UPDATE_RECIPE_WIDGETS = "com.world.jst.android.bakingapp.widget.action.update_recipe_widgets";

    public BakingAppRecipeService() {
        super("BakingAppRecipeService");
    }

    public static void startActionUpdatePlantWidgets(Context context) {
        Intent intent = new Intent(context, BakingAppRecipeService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGETS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "baking_app";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "BakingApp", NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();
            startForeground(1, notification);
        }
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

        Realm realm = Realm.getDefaultInstance();
        ArrayList<IngredientParcelable> ingredientParcelables = new ArrayList<>();
        try {
            Recipe recipe = realm.where(Recipe.class)
                    .equalTo("mId", recipeId)
                    .findFirst();
            RealmList<Ingredient> ingredients = recipe.mIngredients;

            for (Ingredient ingredient : ingredients) {
                ingredientParcelables.add(new IngredientParcelable(ingredient));
            }

            if (recipeName == null) {
                recipeName = recipe.mName;
            }
        } finally {
            realm.close();
        }

        BakingAppWidget.updateAllAppWidget(this, appWidgetManager, appWidgetIds, recipeId,
                recipeName, ingredientParcelables);
    }
}

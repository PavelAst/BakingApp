package com.world.jst.android.bakingapp.utils;

import android.content.Context;
import android.preference.PreferenceManager;

public class CurrentRecipeData {

    private static final String RECIPE_ID = "com.world.jst.android.bakingapp.widget.RECIPE_ID";
    private static final String RECIPE_NAME = "com.world.jst.android.bakingapp.widget.RECIPE_NAME";

    public static int getRecipeId(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(RECIPE_ID, 1);
    }

    public static void setRecipeId(Context context, int recipeId) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putInt(RECIPE_ID, recipeId)
                .apply();
    }

    public static String getRecipeName(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(RECIPE_NAME, null);
    }

    public static void setRecipeName(Context context, String recipeName) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(RECIPE_NAME, recipeName)
                .apply();

    }

}

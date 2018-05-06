package com.world.jst.android.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.world.jst.android.bakingapp.fragment.RecipeFragment;
import com.world.jst.android.bakingapp.model.Recipe;

import io.realm.Realm;

public class RecipeActivity extends SingleFragmentActivity {

    // Turn logging on or off
    private static final boolean L = true;
    public static final String RECIPE_ITEM_ID = "recipe_item_id";
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent initialIntent = getIntent();

        if (initialIntent != null) {
            if (initialIntent.hasExtra(RECIPE_ITEM_ID)) {
                int recipeId = initialIntent.getIntExtra(RECIPE_ITEM_ID, 1);
                Realm realm = Realm.getDefaultInstance();
                try {
                    mRecipe = realm.where(Recipe.class)
                            .equalTo("mId", recipeId)
                            .findFirst();
                } finally {
                    realm.close();
                }
                if (mRecipe != null) {
                    getSupportActionBar().setTitle(mRecipe.mName);
                    if (L) Log.i("RecipeActivity", mRecipe.mIngredients.get(0).mIngredient);
                }
            }
        }
    }

    @Override
    protected Fragment createFragment() {
        return new RecipeFragment();
    }
}

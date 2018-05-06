package com.world.jst.android.bakingapp.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.world.jst.android.bakingapp.fragment.RecipesListFragment;
import com.world.jst.android.bakingapp.model.Recipe;

public class RecipesListActivity extends SingleFragmentActivity
        implements RecipesListFragment.RecipeOnClickHandler {

    @Override
    protected Fragment createFragment() {
        return new RecipesListFragment();
    }

    @Override
    public void onClick(Recipe recipe) {
        Intent intentRecipeDetails = new Intent(this, RecipeActivity.class);
        intentRecipeDetails.putExtra(RecipeActivity.RECIPE_ITEM_ID, recipe.mId);
        startActivity(intentRecipeDetails);
    }
}


package com.world.jst.android.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
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
        Bundle bundle = new Bundle();
        bundle.putInt(RecipeDetailsActivity.RECIPE_ITEM_ID, recipe.mId);
        bundle.putString(RecipeDetailsActivity.RECIPE_ITEM_NAME, recipe.mName);

        Intent intentRecipeDetails = new Intent(this, RecipeDetailsActivity.class);
        intentRecipeDetails.putExtras(bundle);
        startActivity(intentRecipeDetails);
    }
}


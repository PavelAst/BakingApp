package com.world.jst.android.bakingapp.activity;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.world.jst.android.bakingapp.fragment.RecipesListFragment;
import com.world.jst.android.bakingapp.model.Recipe;

public class RecipesActivity extends SingleFragmentActivity
        implements RecipesListFragment.RecipeOnClickHandler {

    @Override
    protected Fragment createFragment() {
        return new RecipesListFragment();
    }

    @Override
    public void onClick(Recipe recipe) {
        Toast.makeText(this, recipe.toString(), Toast.LENGTH_SHORT).show();
    }
}


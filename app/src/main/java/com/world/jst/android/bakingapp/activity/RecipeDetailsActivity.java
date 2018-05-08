package com.world.jst.android.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.world.jst.android.bakingapp.R;
import com.world.jst.android.bakingapp.fragment.RecipeIngredientsFragment;

public class RecipeDetailsActivity extends AppCompatActivity {

    // Turn logging on or off
    private static final boolean L = false;
    public static final String RECIPE_ITEM_ID = "recipe_item_id";
    public static final String RECIPE_ITEM_NAME = "recipe_item_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Intent initialIntent = getIntent();
        int recipeId = initialIntent.getIntExtra(RECIPE_ITEM_ID, 1);
        String recipeName = initialIntent.getStringExtra(RECIPE_ITEM_NAME);
        if (!recipeName.isEmpty()) {
            getSupportActionBar().setTitle(recipeName);
        }

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.ingredients_placeholder);

        if (fragment == null) {
            fragment = RecipeIngredientsFragment.newInstance(recipeId);
            fm.beginTransaction()
                    .add(R.id.ingredients_placeholder, fragment)
                    .commit();
        }
    }
}

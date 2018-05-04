package com.world.jst.android.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.world.jst.android.bakingapp.fragment.RecipeFragment;

public class RecipeActivity extends SingleFragmentActivity {

    public static final String RECIPE_ITEM = "recipe_item_object";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent initialIntent = getIntent();

        if (initialIntent != null) {
            if (initialIntent.hasExtra(RECIPE_ITEM)) {
                String title = initialIntent.getStringExtra(RECIPE_ITEM);
                getSupportActionBar().setTitle(title);
            }
        }
    }

    @Override
    protected Fragment createFragment() {
        return new RecipeFragment();
    }
}

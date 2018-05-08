package com.world.jst.android.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.world.jst.android.bakingapp.R;
import com.world.jst.android.bakingapp.fragment.RecipeIngredientsFragment;
import com.world.jst.android.bakingapp.model.Recipe;

import io.realm.Realm;

public class RecipeDetailsActivity extends AppCompatActivity {

    // Turn logging on or off
    private static final boolean L = true;
    public static final String RECIPE_ITEM_ID = "recipe_item_id";
    public static final String RECIPE_ITEM_NAME = "recipe_item_name";
    private Recipe mRecipe;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        Intent initialIntent = getIntent();

//        if (initialIntent != null) {
//            if (initialIntent.hasExtra(RECIPE_ITEM_ID)) {
                int recipeId = initialIntent.getIntExtra(RECIPE_ITEM_ID, 1);
                String recipeName = initialIntent.getStringExtra(RECIPE_ITEM_NAME);
                if (!recipeName.isEmpty()) {
                    setTitle(recipeName);
                }

//                Realm realm = Realm.getDefaultInstance();
//                try {
//                    mRecipe = realm.where(Recipe.class)
//                            .equalTo("mId", recipeId)
//                            .findFirst();
//                } finally {
//                    realm.close();
//                }

//                if (mRecipe != null) {
////                    getSupportActionBar().setTitle(mRecipe.mName);
////                    mToolbar.setTitle(mRecipe.mName);
//                    setTitle(mRecipe.mName);
//                    if (L) Log.i("RecipeDetailsActivity", mRecipe.mIngredients.get(0).mName);
//                }
//            }
//        }

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

package com.world.jst.android.bakingapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.world.jst.android.bakingapp.R;

import static com.world.jst.android.bakingapp.activity.RecipeDetailsActivity.RECIPE_ITEM_ID;

public class StepDetailsActivity extends AppCompatActivity {

    // Turn logging on or off
    private static final boolean L = true;
    private static final String TAG = "StepDetailsActivity";
    private int mRecipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        Intent initialIntent = getIntent();
        mRecipeId = initialIntent.getIntExtra(RECIPE_ITEM_ID, 1);

        if (L) Log.d(TAG, "mRecipeId = " + mRecipeId);
    }
}

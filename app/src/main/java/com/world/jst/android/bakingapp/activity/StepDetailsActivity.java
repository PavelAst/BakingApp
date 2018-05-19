package com.world.jst.android.bakingapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.world.jst.android.bakingapp.R;
import com.world.jst.android.bakingapp.fragment.StepDetailsFragment;
import com.world.jst.android.bakingapp.model.Recipe;
import com.world.jst.android.bakingapp.model.Step;
import com.world.jst.android.bakingapp.view.SlidingTabLayout;

import io.realm.Realm;
import io.realm.RealmList;


public class StepDetailsActivity extends AppCompatActivity {

    // Turn logging on or off
    private static final boolean L = false;
    private static final String TAG = "StepDetailsActivity";
    public static final String RECIPE_ITEM_NAME = "recipe_item_name";
    public static final String RECIPE_ITEM_ID = "recipe_item_id";
    public static final String STEP_ID = "step_id";
    private Realm mRealm;

    public static Intent newIntent(Context packageContext, String recipeName, int recipeId, int stepId) {
        Intent intent = new Intent(packageContext, StepDetailsActivity.class);
        intent.putExtra(RECIPE_ITEM_NAME, recipeName);
        intent.putExtra(RECIPE_ITEM_ID, recipeId);
        intent.putExtra(STEP_ID, stepId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        Intent initialIntent = getIntent();
        String recipeName = initialIntent.getStringExtra(RECIPE_ITEM_NAME);
        int recipeId = initialIntent.getIntExtra(RECIPE_ITEM_ID, 1);
        int stepId = initialIntent.getIntExtra(STEP_ID, 1);

        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.hide();
            }
        } else {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                if (!TextUtils.isEmpty(recipeName)) {
                    actionBar.setTitle(recipeName);
                }
            }
        }

        if (L) Log.d(TAG, "mRecipeId = " + recipeId);

        /*
          A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0 and
          above, but is designed to give continuous feedback to the user when scrolling.
        */
        SlidingTabLayout slidingTabLayout = findViewById(R.id.steps_sliding_tabs);
        /*
          A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
         */
        ViewPager recipeStepViewPager = findViewById(R.id.recipe_steps_vp);

        mRealm = Realm.getDefaultInstance();

        Recipe recipe = mRealm.where(Recipe.class)
                .equalTo("mId", recipeId)
                .findFirst();
        final RealmList<Step> steps = recipe.mSteps;

        FragmentManager fm = getSupportFragmentManager();
        recipeStepViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Step step = steps.get(position);
                String shortDescription = step.mShortDescription;
                String description = step.mDescription;
                String videoUrl = step.mVideoUrl;
                String thumbnailUrl = step.mThumbnailURL;
                return StepDetailsFragment
                        .newInstance(shortDescription, description, videoUrl, thumbnailUrl, false);
            }

            @Override
            public int getCount() {
                return steps.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return "Step " + (position + 1);
            }
        });
        slidingTabLayout.setViewPager(recipeStepViewPager);

        for (int i = 0; i < steps.size(); i++) {
            if (steps.get(i).mId == stepId) {
                recipeStepViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRealm != null) {
            mRealm.close();
        }
    }

}

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
    private static final boolean L = true;
    private static final String TAG = "StepDetailsActivity";
    public static final String RECIPE_ITEM_ID = "recipe_item_id";
    public static final String STEP_ID = "step_id";
    private int mRecipeId;
    private int mStepId;
    /**
     * A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;
    /**
     * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mRecipeStepViewPager;

    public static Intent newIntent(Context packageContext, int recipeId, int stepId) {
        Intent intent = new Intent(packageContext, StepDetailsActivity.class);
        intent.putExtra(RECIPE_ITEM_ID, recipeId);
        intent.putExtra(STEP_ID, stepId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

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
        }

        Intent initialIntent = getIntent();
        mRecipeId = initialIntent.getIntExtra(RECIPE_ITEM_ID, 1);
        mStepId = initialIntent.getIntExtra(STEP_ID, 1);

        if (L) Log.d(TAG, "mRecipeId = " + mRecipeId);

        mSlidingTabLayout = findViewById(R.id.steps_sliding_tabs);
        mRecipeStepViewPager = findViewById(R.id.recipe_steps_vp);

        Realm realm = Realm.getDefaultInstance();
        Recipe recipe;
        final RealmList<Step> steps;
        try {
            recipe = realm.where(Recipe.class)
                    .equalTo("mId", mRecipeId)
                    .findFirst();
            steps = recipe.mSteps;
        } finally {
            realm.close();
        }

        FragmentManager fm = getSupportFragmentManager();
        mRecipeStepViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
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
        mSlidingTabLayout.setViewPager(mRecipeStepViewPager);

        for (int i = 0; i < steps.size(); i++) {
            if (steps.get(i).mId == mStepId) {
                mRecipeStepViewPager.setCurrentItem(i);
                break;
            }
        }
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//        int currentOrientation = newConfig.orientation;
//        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
//            mSlidingTabLayout.setVisibility(View.INVISIBLE);
//        } else {
//            mSlidingTabLayout.setVisibility(View.VISIBLE);
//        }
//    }
}

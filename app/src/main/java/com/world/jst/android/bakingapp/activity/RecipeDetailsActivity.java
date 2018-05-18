package com.world.jst.android.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.world.jst.android.bakingapp.R;
import com.world.jst.android.bakingapp.fragment.RecipeIngredientsFragment;
import com.world.jst.android.bakingapp.fragment.RecipeStepsFragment;
import com.world.jst.android.bakingapp.fragment.StepDetailsFragment;
import com.world.jst.android.bakingapp.model.Recipe;
import com.world.jst.android.bakingapp.model.Step;
import com.world.jst.android.bakingapp.view.SlidingTabLayout;

import io.realm.Realm;
import io.realm.RealmList;

public class RecipeDetailsActivity extends AppCompatActivity
        implements RecipeStepsFragment.RecipeStepsOnClickHandler {

    // Turn logging on or off
    private static final boolean L = false;
    private static final String TAG = "RecipeDetailsActivity";
    public static final String RECIPE_ITEM_ID = "recipe_item_id";
    public static final String RECIPE_ITEM_NAME = "recipe_item_name";
    private boolean mTwoPane;
    private String mRecipeName;
    private int mRecipeId;
    private Realm mRealm;
    /**
     * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mRecipeStepViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent initialIntent = getIntent();
        mRecipeId = initialIntent.getIntExtra(RECIPE_ITEM_ID, 1);
        mRecipeName = initialIntent.getStringExtra(RECIPE_ITEM_NAME);
        if (mRecipeName != null && !mRecipeName.isEmpty()) {
            getSupportActionBar().setTitle(mRecipeName);
        }

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragmentIngredients = fm.findFragmentById(R.id.recipe_ingredients_container);
        if (fragmentIngredients == null) {
            fragmentIngredients = RecipeIngredientsFragment.newInstance(mRecipeId);
            fm.beginTransaction()
                    .add(R.id.recipe_ingredients_container, fragmentIngredients)
                    .commit();
        }

        Fragment fragmentSteps = fm.findFragmentById(R.id.recipe_steps_container);
        if (fragmentSteps == null) {
            fragmentSteps = RecipeStepsFragment.newInstance(mRecipeId);
            fm.beginTransaction()
                    .add(R.id.recipe_steps_container, fragmentSteps)
                    .commit();
        }

        if (findViewById(R.id.steps_linear_layout) != null) {
            mTwoPane = true;
            if (L) Log.d(TAG, "mTwoPane = true");
            /*
      A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0 and
      above, but is designed to give continuous feedback to the user when scrolling.
     */
            SlidingTabLayout slidingTabLayout = findViewById(R.id.steps_sliding_tabs);
            mRecipeStepViewPager = findViewById(R.id.recipe_steps_vp);

            mRealm = Realm.getDefaultInstance();

            Recipe recipe = mRealm.where(Recipe.class)
                    .equalTo("mId", mRecipeId)
                    .findFirst();
            final RealmList<Step> steps = recipe.mSteps;

            mRecipeStepViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
                @Override
                public Fragment getItem(int position) {
                    Step step = steps.get(position);
                    String shortDescription = step.mShortDescription;
                    String description = step.mDescription;
                    String videoUrl = step.mVideoUrl;
                    String thumbnailUrl = step.mThumbnailURL;
                    return StepDetailsFragment
                            .newInstance(shortDescription, description, videoUrl, thumbnailUrl, true);
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
            slidingTabLayout.setViewPager(mRecipeStepViewPager);

        } else {
            // We're in single-pane mode and displaying fragments on a phone in separate activities
            mTwoPane = false;
            if (L) Log.d(TAG, "mTwoPane = false");
        }
    }

    @Override
    public void onOptionClick(Step step, int position) {
        if (mTwoPane) {
            mRecipeStepViewPager.setCurrentItem(position);
        } else {
            Intent intentStepDetails = StepDetailsActivity
                    .newIntent(this, mRecipeName, mRecipeId, step.mId);
            startActivity(intentStepDetails);
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

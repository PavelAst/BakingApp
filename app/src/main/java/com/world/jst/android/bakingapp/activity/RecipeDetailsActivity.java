package com.world.jst.android.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

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
    private int mRecipeId;
    /**
     * A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;
    /**
     * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mRecipeStepViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        Intent initialIntent = getIntent();
        mRecipeId = initialIntent.getIntExtra(RECIPE_ITEM_ID, 1);
        String recipeName = initialIntent.getStringExtra(RECIPE_ITEM_NAME);
        if (!recipeName.isEmpty()) {
            getSupportActionBar().setTitle(recipeName);
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

            mRecipeStepViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
                @Override
                public Fragment getItem(int position) {
                    Step step = steps.get(position);
                    String shortDescription = step.mShortDescription;
                    String description = step.mDescription;
                    String videoUrl = step.mVideoUrl;
                    String thumbnailUrl = step.mThumbnailURL;
                    return StepDetailsFragment
                            .newInstance(shortDescription, description, videoUrl, thumbnailUrl);
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

        } else {
            // We're in single-pane mode and displaying fragments on a phone in separate activities
            mTwoPane = false;
            if (L) Log.d(TAG, "mTwoPane = false");
        }
    }

    @Override
    public void onOptionClick(Step step) {
        if (mTwoPane) {
            Toast.makeText(this, step.mShortDescription, Toast.LENGTH_SHORT).show();
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt(RECIPE_ITEM_ID, mRecipeId);

            Intent intentRecipeDetails = new Intent(this, StepDetailsActivity.class);
            intentRecipeDetails.putExtras(bundle);
            startActivity(intentRecipeDetails);
        }
    }
}

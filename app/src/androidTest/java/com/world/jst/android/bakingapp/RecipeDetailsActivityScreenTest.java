package com.world.jst.android.bakingapp;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.world.jst.android.bakingapp.activity.RecipesListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailsActivityScreenTest {

    public static final String RECIPE_NAME = "Cheesecake";

    @Rule
    public ActivityTestRule<RecipesListActivity> mActivityTestRule = new ActivityTestRule<>(RecipesListActivity.class);

    @Test
    public void clickRecyclerViewItem_OpensRecipeDetailsActivity() {

        onView(ViewMatchers.withId(R.id.recipes_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
        onView(ViewMatchers.withId(R.id.recipe_steps_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withText(RECIPE_NAME)).check(matches(isDisplayed()));
    }
}

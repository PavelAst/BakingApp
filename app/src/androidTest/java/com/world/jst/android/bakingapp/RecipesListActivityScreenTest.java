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
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.world.jst.android.bakingapp.TestUtils.atPosition;

@RunWith(AndroidJUnit4.class)
public class RecipesListActivityScreenTest {

    public static final String RECIPE_NAME = "Brownies";

    @Rule
    public ActivityTestRule<RecipesListActivity> mActivityTestRule = new ActivityTestRule<>(RecipesListActivity.class);

    @Test
    public void clickRecyclerViewItem_OpensRecipeDetailsActivity() {

        onView(ViewMatchers.withId(R.id.recipes_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        // Checks that the RecipeDetailsActivity opens with the correct recipe name displayed
        onView(withText(RECIPE_NAME)).check(matches(isDisplayed()));
    }

    @Test
    public void clickRecyclerViewItem_OpensIngredientsList() {

        onView(ViewMatchers.withId(R.id.recipes_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Check the first item of ingredients list for first recipe - "Nutella Pie"
        onView(withId(R.id.recipe_ingredients_rv))
                .check(matches(atPosition(0, hasDescendant(withText("2")))));
        onView(withId(R.id.recipe_ingredients_rv))
                .check(matches(atPosition(0, hasDescendant(withText("CUP")))));
        onView(withId(R.id.recipe_ingredients_rv))
                .check(matches(atPosition(0, hasDescendant(withText("Graham Cracker crumbs")))));

        // Check the last item of ingredients list for first recipe - "Nutella Pie"
        onView(withId(R.id.recipe_ingredients_rv))
                .check(matches(atPosition(8, hasDescendant(withText("4")))));
        onView(withId(R.id.recipe_ingredients_rv))
                .check(matches(atPosition(8, hasDescendant(withText("OZ")))));
        onView(withId(R.id.recipe_ingredients_rv))
                .check(matches(atPosition(8, hasDescendant(withText("cream cheese(softened)")))));
    }

    @Test
    public void clickRecyclerViewItem_OpensStepsList() {
        onView(ViewMatchers.withId(R.id.recipes_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.recipe_steps_rv))
                .perform(scrollToPosition(0))
                .check(matches(atPosition(0, hasDescendant(withText("Recipe Introduction")))));

        onView(withId(R.id.recipe_steps_rv))
                .perform(scrollToPosition(6))
                .check(matches(atPosition(6, hasDescendant(withText("Finishing Steps")))));
    }

}

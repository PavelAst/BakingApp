package com.world.jst.android.bakingapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Recipe {

    @SerializedName("id")
    public int mId;

    @SerializedName("name")
    public String mName;

    @SerializedName("ingredients")
    public List<Ingredient> mIngredients;

    @SerializedName("steps")
    public List<Step> mSteps;

    @SerializedName("servings")
    public String mServings;

    @SerializedName("image")
    public String mImage;

    @Override
    public String toString() {
        return mId + ". " + mName + " for " + mServings + " servings.";
    }
}

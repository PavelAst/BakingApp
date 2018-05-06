package com.world.jst.android.bakingapp.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Recipe extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    public int mId;

    @Required
    @SerializedName("name")
    public String mName;

    @SerializedName("ingredients")
    public RealmList<Ingredient> mIngredients;

    @SerializedName("steps")
    public RealmList<Step> mSteps;

    @SerializedName("servings")
    public String mServings;

    @SerializedName("image")
    public String mImage;

    @Override
    public String toString() {
        return mId + ". " + mName + " for " + mServings + " servings.";
    }
}

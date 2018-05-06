package com.world.jst.android.bakingapp.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Ingredient extends RealmObject {

    @SerializedName("quantity")
    public float mQuantity;

    @SerializedName("measure")
    public String mMeasure;

    @SerializedName("ingredient")
    public String mIngredient;

}

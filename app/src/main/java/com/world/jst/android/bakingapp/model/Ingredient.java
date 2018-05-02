package com.world.jst.android.bakingapp.model;

import com.google.gson.annotations.SerializedName;

public class Ingredient {

    @SerializedName("quantity")
    public float mQuantity;

    @SerializedName("measure")
    public String mMeasure;

    @SerializedName("ingredient")
    public String mIngredient;

}

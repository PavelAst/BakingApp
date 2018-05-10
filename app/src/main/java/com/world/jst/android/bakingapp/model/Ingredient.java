package com.world.jst.android.bakingapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Ingredient extends RealmObject {

    @PrimaryKey
    public String mId = UUID.randomUUID().toString();

    @SerializedName("quantity")
    public float mQuantity;

    @SerializedName("measure")
    public String mMeasure;

    @SerializedName("ingredient")
    public String mName;

}

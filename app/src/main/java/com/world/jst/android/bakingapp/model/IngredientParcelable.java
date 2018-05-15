package com.world.jst.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class IngredientParcelable implements Parcelable {

    private float mQuantity;
    private String mMeasure;
    private String mName;

    public IngredientParcelable(float quantity, String measure, String name) {
        mQuantity = quantity;
        mMeasure = measure;
        mName = name;
    }

    public IngredientParcelable(Ingredient ingredient) {
        mQuantity = ingredient.mQuantity;
        mMeasure = ingredient.mMeasure;
        mName = ingredient.mName;
    }

    public float getQuantity() {
        return mQuantity;
    }

    public void setQuantity(float quantity) {
        mQuantity = quantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public void setMeasure(String measure) {
        mMeasure = measure;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public String toString() {
        String q = String.valueOf(mQuantity);
        String quantity = !q.contains(".") ? q : q.replaceAll("0*$", "")
                .replaceAll("\\.$", "");
        return quantity + " " + mMeasure + "  " + mName + "\n";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.mQuantity);
        dest.writeString(this.mMeasure);
        dest.writeString(this.mName);
    }

    protected IngredientParcelable(Parcel in) {
        this.mQuantity = in.readFloat();
        this.mMeasure = in.readString();
        this.mName = in.readString();
    }

    public static final Creator<IngredientParcelable> CREATOR = new Creator<IngredientParcelable>() {
        @Override
        public IngredientParcelable createFromParcel(Parcel source) {
            return new IngredientParcelable(source);
        }

        @Override
        public IngredientParcelable[] newArray(int size) {
            return new IngredientParcelable[size];
        }
    };
}

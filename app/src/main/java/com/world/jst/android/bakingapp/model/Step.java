package com.world.jst.android.bakingapp.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Step extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    public int mId;

    @SerializedName("shortDescription")
    public String mShortDescription;

    @SerializedName("description")
    public String mDescription;

    @SerializedName("videoURL")
    public String mVideoUrl;

    @SerializedName("thumbnailURL")
    public String mThumbnailURL;
}

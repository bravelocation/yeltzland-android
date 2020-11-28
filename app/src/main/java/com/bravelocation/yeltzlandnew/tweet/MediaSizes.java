package com.bravelocation.yeltzlandnew.tweet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class MediaSizes {
    @SerializedName("thumb")
    @Expose
    MediaSize thumb;

    @SerializedName("large")
    @Expose
    MediaSize large;

    @SerializedName("medium")
    @Expose
    MediaSize medium;

    @SerializedName("small")
    @Expose
    MediaSize small;
}
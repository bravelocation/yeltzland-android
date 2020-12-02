package com.bravelocation.yeltzlandnew.tweet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class MediaSize {
    @SerializedName("h")
    @Expose
    int height;

    @SerializedName("resize")
    @Expose
    String resize;

    @SerializedName("w")
    @Expose
    int width;
}
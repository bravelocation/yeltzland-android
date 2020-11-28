package com.bravelocation.yeltzlandnew.tweet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class User {
    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("screen_name")
    @Expose
    public String screenName;

    @SerializedName("profile_image_url_https")
    @Expose
    public String profileImageUrl;
}

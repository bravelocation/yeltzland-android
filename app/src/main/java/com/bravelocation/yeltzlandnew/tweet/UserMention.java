package com.bravelocation.yeltzlandnew.tweet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

class UserMention implements TweetEntity {

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("screen_name")
    @Expose
    public String screenName;

    @SerializedName("id_str")
    @Expose
    public String id;

    @SerializedName("indices")
    @Expose
    public List<Integer> indices = null;

    public String displayText() {
        return "@" + this.screenName;
    }

    public String linkUrl() {
        return "https://twitter.com/" + this.screenName;
    }
}
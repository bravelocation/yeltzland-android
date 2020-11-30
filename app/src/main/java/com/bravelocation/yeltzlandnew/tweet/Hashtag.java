package com.bravelocation.yeltzlandnew.tweet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

class Hashtag implements TweetEntity {
    @SerializedName("text")
    @Expose
    public String text;

    @SerializedName("indices")
    @Expose
    public List<Integer> indices = null;

    public List<Integer> getIndices() { return this.indices; }

    public String displayText() {
        return "#" + this.text;
    }

    public String linkUrl() {
        return "https://twitter.com/hashtag/" + this.text;
    }
}

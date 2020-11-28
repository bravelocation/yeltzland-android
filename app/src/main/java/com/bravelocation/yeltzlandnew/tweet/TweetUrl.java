package com.bravelocation.yeltzlandnew.tweet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

class TweetUrl implements TweetEntity {
    @SerializedName("url")
    @Expose
    public String url;

    @SerializedName("display_url")
    @Expose
    public String displayUrl;

    @SerializedName("expanded_url")
    @Expose
    public String expandedUrl;

    @SerializedName("indices")
    @Expose
    public List<Integer> indices = null;

    public String displayText() {
        if (this.expandedUrl.startsWith("https://twitter.com")) {
            return "";
        }

        return this.expandedUrl;
    }

    public String linkUrl() {
        return this.displayText();
    }
}
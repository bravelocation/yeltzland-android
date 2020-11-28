package com.bravelocation.yeltzlandnew.tweet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

class Media implements TweetEntity {
    @SerializedName("id_str")
    @Expose
    public String id;

    @SerializedName("display_url")
    @Expose
    public String displayUrl;

    @SerializedName("expanded_url")
    @Expose
    public String expandedUrl;

    @SerializedName("media_url_https")
    @Expose
    public String mediaUrl;

    @SerializedName("sizes")
    @Expose
    public MediaSizes sizes = null;

    @SerializedName("indices")
    @Expose
    public List<Integer> indices = null;

    public String displayText() {
        return "";
    }

    public String linkUrl() {
        return expandedUrl;
    }
}
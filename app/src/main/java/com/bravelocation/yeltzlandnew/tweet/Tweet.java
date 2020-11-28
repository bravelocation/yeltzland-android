package com.bravelocation.yeltzlandnew.tweet;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import com.google.gson.annotations.SerializedName;

public class Tweet implements DisplayTweet {
    @SerializedName("id_str")
    String id;

    @SerializedName("full_text")
    public String fullText;

    @SerializedName("user")
    User user;

    @SerializedName("created_at")
    Date createdAt;

    @SerializedName("entities")
    Entities entities;

    @SerializedName("extended_entities")
    ExtendedEntities extendedEntities;

    @SerializedName("retweeted_status")
    Retweet retweet;

    @SerializedName("quoted_status")
    QuotedTweet quotedTweet;

    public boolean isRetweet() {
        return false;
    }

    public QuotedTweet quote() {
        return quotedTweet;
    }

    public URL userTwitterUrl() throws MalformedURLException {
        return new URL("https://twitter.com/" + this.user.screenName);
    }

    public URL bodyTwitterUrl() throws MalformedURLException {
        return new URL("https://twitter.com/" + this.user.screenName + "/status/" + this.id);
    }
}





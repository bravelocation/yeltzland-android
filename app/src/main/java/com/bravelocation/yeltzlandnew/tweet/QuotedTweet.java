package com.bravelocation.yeltzlandnew.tweet;

import com.bravelocation.yeltzlandnew.tweet.DisplayTweet;
import com.bravelocation.yeltzlandnew.tweet.Entities;
import com.bravelocation.yeltzlandnew.tweet.ExtendedEntities;
import com.bravelocation.yeltzlandnew.tweet.User;
import com.google.gson.annotations.SerializedName;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

class QuotedTweet implements DisplayTweet {
    @SerializedName("id_str")
    String id;

    @SerializedName("full_text")
    String fullText;

    @SerializedName("user")
    User user;

    @SerializedName("created_at")
    Date createdAt;

    @SerializedName("entities")
    Entities entities;

    @SerializedName("extended_entities")
    ExtendedEntities extendedEntities;

    @SerializedName("quoted_status")
    QuotedTweet quotedTweet;

    public boolean isRetweet() {
        return true;
    }

    public QuotedTweet quote() {
        return quotedTweet;
    }

    public String userTwitterUrl() {
        return "https://twitter.com/" + this.user.screenName;
    }

    public String bodyTwitterUrl() {
        return "https://twitter.com/" + this.user.screenName + "/status/" + this.id;
    }
}
package com.bravelocation.yeltzlandnew.tweet;

import java.util.Date;
import com.google.gson.annotations.SerializedName;

public class Tweet implements DisplayTweet {
    @SerializedName("id_str")
    String id;

    @SerializedName("full_text")
    public String fullText;

    @SerializedName("user")
    public User user;

    @SerializedName("created_at")
    Date createdAt;

    @SerializedName("entities")
    Entities entities;

    @SerializedName("extended_entities")
    ExtendedEntities extendedEntities;

    @SerializedName("retweeted_status")
    public Retweet retweet;

    @SerializedName("quoted_status")
    QuotedTweet quotedTweet;

    public String getFullText() { return this.fullText; }
    public User getUser() { return this.user; }
    public Date getCreatedDate() { return this.createdAt; }
    public Entities getEntities() { return this.entities; }
    public ExtendedEntities getExtendedEntities() { return this.extendedEntities; }

    public boolean isRetweet() {
        return false;
    }
    public boolean hasRetweet() {
        return this.retweet != null;
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





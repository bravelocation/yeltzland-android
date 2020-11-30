package com.bravelocation.yeltzlandnew.tweet;

import com.bravelocation.yeltzlandnew.tweet.TweetSymbol;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Entities {
    @SerializedName("hashtags")
    @Expose
    public List<Hashtag> hashtags = null;

    @SerializedName("urls")
    @Expose
    public List<TweetUrl> urls = null;

    @SerializedName("user_mentions")
    @Expose
    public List<UserMention> userMentions = null;

    @SerializedName("symbols")
    @Expose
    public List<TweetSymbol> symbols = null;
}
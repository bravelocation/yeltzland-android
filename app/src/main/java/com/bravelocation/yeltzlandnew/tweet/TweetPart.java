package com.bravelocation.yeltzlandnew.tweet;

public class TweetPart {
    public TweetPart(String text, String linkUrl) {
        this.text = text;
        this.linkUrl = linkUrl;
    }

    public String text;
    public String linkUrl;

    public boolean highlight() {
        return this.linkUrl != null;
    }
}

package com.bravelocation.yeltzlandnew.tweet;

import java.net.MalformedURLException;
import java.net.URL;

public interface DisplayTweet {
    String getFullText();
    User getUser();

    DisplayTweet quote();
    boolean isRetweet();

    String userTwitterUrl();
    String bodyTwitterUrl();
}
package com.bravelocation.yeltzlandnew.tweet;

import java.net.MalformedURLException;
import java.net.URL;

public interface DisplayTweet {

    DisplayTweet quote();
    boolean isRetweet();

    String userTwitterUrl();
    String bodyTwitterUrl();
}
package com.bravelocation.yeltzlandnew.tweet;

import java.net.MalformedURLException;
import java.net.URL;

public interface DisplayTweet {

    DisplayTweet quote();
    boolean isRetweet();

    URL userTwitterUrl() throws MalformedURLException;
    URL bodyTwitterUrl() throws MalformedURLException;
}
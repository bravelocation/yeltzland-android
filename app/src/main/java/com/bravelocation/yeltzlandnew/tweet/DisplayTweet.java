package com.bravelocation.yeltzlandnew.tweet;
import java.util.Date;

public interface DisplayTweet {
    String getFullText();
    User getUser();
    Date getCreatedDate();

    DisplayTweet quote();
    boolean isRetweet();

    String userTwitterUrl();
    String bodyTwitterUrl();
}
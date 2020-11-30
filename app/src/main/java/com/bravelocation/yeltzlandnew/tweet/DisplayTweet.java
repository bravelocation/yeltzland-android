package com.bravelocation.yeltzlandnew.tweet;
import java.util.Date;

public interface DisplayTweet {
    String getFullText();
    User getUser();
    Date getCreatedDate();
    Entities getEntities();
    ExtendedEntities getExtendedEntities();

    DisplayTweet quote();
    boolean isRetweet();

    String userTwitterUrl();
    String bodyTwitterUrl();
}
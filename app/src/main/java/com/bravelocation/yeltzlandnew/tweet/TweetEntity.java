package com.bravelocation.yeltzlandnew.tweet;

import java.util.List;

public interface TweetEntity {
    List<Integer> getIndices();
    String displayText();
    String linkUrl();
}
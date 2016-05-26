package com.bravelocation.yeltzland;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class TwitterFragment extends ListFragment {

    public TwitterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final UserTimeline userTimeline = new UserTimeline.Builder().screenName("halesowentownfc").build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                .setTimeline(userTimeline)
                .build();
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_twitter, container, false);
    }

    public void reload() {
        TweetTimelineListAdapter adapter = (TweetTimelineListAdapter) this.getListAdapter();
        adapter.refresh(new Callback<TimelineResult<Tweet>>() {
            // Do nothing on failure for now
            @Override
            public void success(Result<TimelineResult<Tweet>> result) {}

            @Override
            public void failure(TwitterException exception) {}

        });
    }
}
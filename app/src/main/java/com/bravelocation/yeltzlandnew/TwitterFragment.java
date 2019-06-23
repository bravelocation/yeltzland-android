package com.bravelocation.yeltzlandnew;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class TwitterFragment extends ListFragment {

    private TweetTimelineListAdapter adapter;
    private SwipeRefreshLayout swipeLayout;
    private Handler refreshHandler;
    private final int RELOAD_INTERVAL = 1000 * 60 * 5;

    private final Callback<TimelineResult<Tweet>> timelineRefreshCallback = new Callback<TimelineResult<Tweet>>() {
        // Do nothing for now
        @Override
        public void success(Result<TimelineResult<Tweet>> result) {
            if (swipeLayout != null) {
                swipeLayout.setRefreshing(false);
            }
        }

        @Override
        public void failure(TwitterException exception) {
            if (swipeLayout != null) {
                swipeLayout.setRefreshing(false);
            }
        }
    };

    public TwitterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final UserTimeline userTimeline = new UserTimeline.Builder().screenName("halesowentownfc").build();
        final FragmentActivity currentContext = getActivity();

        userTimeline.next(null, new Callback<TimelineResult<Tweet>>() {
            // Do nothing for now
            @Override
            public void success(Result<TimelineResult<Tweet>> result) {
                adapter = new TweetTimelineListAdapter.Builder(currentContext)
                        .setTimeline(userTimeline)
                        .build();

                setListAdapter(adapter);
                setupTimedReload();
            }

            @Override
            public void failure(TwitterException exception) {
                //
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_twitter, container, false);

        this.swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        this.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                 reload();
            }
        });

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Log.d("TwitterFragment", "Reloading fragment now not visible");
            this.reload();
        }
    }

    // Reload action - either reload button or swipe
    public void reload() {
        if (this.swipeLayout != null) {
            this.swipeLayout.setRefreshing(true);
        }

        this.reloadTimeline();
        this.resetTimedReload();
    }

    // Actually reload timeline
    private void reloadTimeline() {
        if (this.adapter != null) {
            this.adapter.refresh(this.timelineRefreshCallback);
        }
    }

    // Timer refresh functions
    private void setupTimedReload() {
        this.refreshHandler = new Handler();
        this.refreshHandler.postDelayed(refreshTimeline, RELOAD_INTERVAL);
    }

    private void resetTimedReload() {
        if (this.refreshHandler != null) {
            this.refreshHandler.removeCallbacks(this.refreshTimeline);
        }

        this.setupTimedReload();
    }

    private Runnable refreshTimeline = new Runnable() {
        @Override
        public void run() {
            reloadTimeline();
            refreshHandler.postDelayed(refreshTimeline, RELOAD_INTERVAL);
        }
    };
}
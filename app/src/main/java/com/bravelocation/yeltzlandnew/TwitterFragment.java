package com.bravelocation.yeltzlandnew;

import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.ListFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TwitterFragment extends ListFragment {

    private SwipeRefreshLayout swipeLayout;
    private Handler refreshHandler;
    private final int RELOAD_INTERVAL = 1000 * 60 * 5;

    private TwitterListAdapter listAdapter;
    TwitterDataProvider dataProvider;

    private final Runnable timelineRefreshCallback = new Runnable() {
        // Do nothing for now
        @Override
        public void run() {
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

        this.dataProvider = new TwitterDataProvider(BuildConfig.TwitterKey, BuildConfig.TwitterSecret, "halesowentownfc", 20);
        this.listAdapter = new TwitterListAdapter(getContext(), this.dataProvider);

        this.listAdapter.refresh(new SetupTimeline(getActivity(), this));
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
        if (this.listAdapter != null) {
            this.listAdapter.refresh(this.timelineRefreshCallback);
        }
    }

    // Timer refresh functions
    private void setupTimedReload() {
        //this.refreshHandler = new Handler();
        //this.refreshHandler.postDelayed(refreshTimeline, RELOAD_INTERVAL);
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

    private class SetupTimeline implements Runnable {

        FragmentActivity activity;
        TwitterFragment fragment;

        SetupTimeline(FragmentActivity activity, TwitterFragment fragment) {
            this.activity = activity;
            this.fragment = fragment;
        }

        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    fragment.setListAdapter(listAdapter);
                    fragment.setupTimedReload();
                }
            });
        }
    }
}
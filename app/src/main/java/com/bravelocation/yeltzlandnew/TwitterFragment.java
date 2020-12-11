package com.bravelocation.yeltzlandnew;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.ListFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TwitterFragment extends ListFragment {

    private SwipeRefreshLayout swipeLayout;
    private Handler refreshHandler;
    private final int RELOAD_INTERVAL = 1000 * 60 * 5;

    private TwitterListAdapter listAdapter;
    TwitterDataProvider dataProvider;
    RefreshTimeline refreshTimeline;

    public TwitterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.refreshTimeline = new RefreshTimeline(getActivity());

        this.dataProvider = new TwitterDataProvider(BuildConfig.TwitterKey, BuildConfig.TwitterSecret, "halesowentownfc", 50);
        this.listAdapter = new TwitterListAdapter(getContext(), this.dataProvider);

        this.listAdapter.refresh(new SetupTimeline(getActivity(), this));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_twitter, container, false);

        // Add a footer to the list view
        ListView list = (ListView) view.findViewById(android.R.id.list);

        ViewGroup viewGroup = (ViewGroup) list;
        LinearLayout footer = (LinearLayout) inflater.inflate(R.layout.tweet_list_footer, viewGroup, false);

        TextView linkText = footer.findViewById(R.id.seeAllTweetsText);
        linkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/halesowentownfc"));
                startActivity(browserIntent);
            }
        });

        list.addFooterView(footer);

        this.swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        this.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                 reload();
            }
        });

        // Start showing the loader
        this.swipeLayout.setRefreshing(true);



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
            this.listAdapter.refresh(this.refreshTimeline);
        }
    }

    // Timer refresh functions
    private void setupTimedReload() {
        this.refreshHandler = new Handler();
        this.refreshHandler.postDelayed(this.refreshTimeline, RELOAD_INTERVAL);
    }

    private void resetTimedReload() {
        if (this.refreshHandler != null) {
            this.refreshHandler.removeCallbacks(this.refreshTimeline);
        }

        this.setupTimedReload();
    }

    private class RefreshTimeline implements Runnable {
        FragmentActivity activity;

        RefreshTimeline(FragmentActivity activity) {
            this.activity = activity;
        }

        @Override
        public void run() {
            listAdapter.refresh(new Runnable() {
                @Override
                public void run() {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (swipeLayout != null) {
                                swipeLayout.setRefreshing(false);
                            }

                            if (listAdapter != null) {
                                listAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            });
        }
    }

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

                    if (fragment.swipeLayout != null) {
                        fragment.swipeLayout.setRefreshing(false);
                    }
                }
            });
        }
    }
}
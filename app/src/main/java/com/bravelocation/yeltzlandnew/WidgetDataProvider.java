package com.bravelocation.yeltzlandnew;

import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;

import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 28/11/2017.
 */

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    Context context;
    Intent intent;
    TimelineManager timelineManager;

    private void initData() {
        this.timelineManager.loadLatestData();
    }

    public WidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;

        this.timelineManager = new TimelineManager();
    }

    @Override
    public void onCreate() {
        this.initData();
    }

    @Override
    public void onDataSetChanged() {
        this.initData();
    }

    @Override
    public void onDestroy() {}

    @Override
    public int getCount() {
        return this.timelineManager.timelineEntries().size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        TimelineDataItem fixture = this.timelineManager.timelineEntries().get(i);

        RemoteViews remoteView = new RemoteViews(this.context.getPackageName(),R.layout.widget_fixture_item);

        remoteView.setTextViewText(R.id.opponent, fixture.opponentPlusHomeAway());

        if (fixture.status == TimelineDataItem.TimelineFixtureStatus.result) {
            remoteView.setTextViewText(R.id.fixtureStatus, "RESULT");
            remoteView.setTextViewText(R.id.scoreordate, fixture.score());
        } else if (fixture.status == TimelineDataItem.TimelineFixtureStatus.inProgress) {
            remoteView.setTextViewText(R.id.fixtureStatus, "LATEST SCORE");
            remoteView.setTextViewText(R.id.scoreordate, fixture.score());
        } else if (fixture.status == TimelineDataItem.TimelineFixtureStatus.fixture) {
            remoteView.setTextViewText(R.id.fixtureStatus, "FIXTURE");
            remoteView.setTextViewText(R.id.scoreordate, fixture.kickoffTime());
        }

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

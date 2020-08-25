package com.bravelocation.yeltzlandnew;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    Context context;
    Intent intent;

    private void initData() {
        TimelineManager.getInstance().fetchLatestData(this.context, null);
    }

    public WidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;

        this.initData();
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
        return TimelineManager.getInstance().timelineEntries().size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        TimelineDataItem fixture = TimelineManager.getInstance().timelineEntries().get(i);

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

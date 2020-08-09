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
    List<FixtureListDataItem> fixtures = new ArrayList<FixtureListDataItem>();
    Context context;
    Intent intent;

    private void initData() {
        fixtures.clear();

        // Add the last results
        fixtures.addAll(FixtureListDataPump.getLastResults(1));

        // Add the next matches
        fixtures.addAll(FixtureListDataPump.getNextFixtures(6));
    }

    public WidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
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
        return this.fixtures.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        FixtureListDataItem fixture = this.fixtures.get(i);

        RemoteViews remoteView = new RemoteViews(this.context.getPackageName(),R.layout.widget_fixture_item);

        remoteView.setTextViewText(R.id.opponent, fixture.displayOpponent());
        remoteView.setTextViewText(R.id.scoreordate, fixture.fullDetails());

        /*
        // Set colors
        if (fixture.teamScore != null && fixture.opponentScore != null) {
            if (fixture.teamScore > fixture.opponentScore) {
                remoteView.setTextColor(R.id.opponent,ContextCompat.getColor(context, R.color.matchWin));
                remoteView.setTextColor(R.id.scoreordate,ContextCompat.getColor(context, R.color.matchWin));
            } else if (fixture.teamScore < fixture.opponentScore) {
                remoteView.setTextColor(R.id.opponent,ContextCompat.getColor(context, R.color.matchLose));
                remoteView.setTextColor(R.id.scoreordate,ContextCompat.getColor(context, R.color.matchLose));
            } else {
                remoteView.setTextColor(R.id.opponent,ContextCompat.getColor(context, R.color.matchDraw));
                remoteView.setTextColor(R.id.scoreordate,ContextCompat.getColor(context, R.color.matchDraw));
            }
        } else {
            remoteView.setTextColor(R.id.opponent,ContextCompat.getColor(context, R.color.matchNone));
            remoteView.setTextColor(R.id.scoreordate,ContextCompat.getColor(context, R.color.matchNone));
        }
        */

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

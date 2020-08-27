package com.bravelocation.yeltzlandnew;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.List;

public class WidgetUpdater implements Runnable {
    private AppWidgetManager appWidgetManager;
    private int[] appWidgetIds;
    private Context context;

    WidgetUpdater(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        this.appWidgetManager = appWidgetManager;
        this.appWidgetIds = appWidgetIds;
        this.context = context;
    }

    @Override
    public void run() {
        if (this.appWidgetManager != null && this.appWidgetIds != null) {
            // There may be multiple widgets active, so update all of them

            for (int appWidgetId : this.appWidgetIds) {
                Log.d("YeltzlandWidget", "Updating widget with ID " + appWidgetId);

                // Construct the RemoteViews object
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.yeltzland_widget);

                // Update the views with timeline info
                List<TimelineDataItem> timelineEntries = TimelineManager.getInstance().timelineEntries();

                if (timelineEntries.size() > 0) {
                    TimelineDataItem fixture = timelineEntries.get(0);
                    views.setTextViewText(R.id.opponent1, fixture.opponentPlusHomeAway());

                    if (fixture.status == TimelineDataItem.TimelineFixtureStatus.result) {
                        views.setTextViewText(R.id.fixtureStatus1, "RESULT");
                        views.setTextViewText(R.id.scoreordate1, fixture.score());
                    } else if (fixture.status == TimelineDataItem.TimelineFixtureStatus.inProgress) {
                        views.setTextViewText(R.id.fixtureStatus1, "LATEST SCORE");
                        views.setTextViewText(R.id.scoreordate1, fixture.score());
                    } else if (fixture.status == TimelineDataItem.TimelineFixtureStatus.fixture) {
                        views.setTextViewText(R.id.fixtureStatus1, "FIXTURE");
                        views.setTextViewText(R.id.scoreordate1, fixture.kickoffTime());
                    }
                }

                if (timelineEntries.size() > 1) {
                    TimelineDataItem fixture = timelineEntries.get(1);
                    views.setTextViewText(R.id.opponent2, fixture.opponentPlusHomeAway());

                    if (fixture.status == TimelineDataItem.TimelineFixtureStatus.result) {
                        views.setTextViewText(R.id.fixtureStatus2, "RESULT");
                        views.setTextViewText(R.id.scoreordate2, fixture.score());
                    } else if (fixture.status == TimelineDataItem.TimelineFixtureStatus.inProgress) {
                        views.setTextViewText(R.id.fixtureStatus2, "LATEST SCORE");
                        views.setTextViewText(R.id.scoreordate2, fixture.score());
                    } else if (fixture.status == TimelineDataItem.TimelineFixtureStatus.fixture) {
                        views.setTextViewText(R.id.fixtureStatus2, "FIXTURE");
                        views.setTextViewText(R.id.scoreordate2, fixture.kickoffTime());
                    }
                }

                // Instruct the widget manager to update the widget
                this.appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        } else {
            Log.d("YeltzlandWidget", "No widgets to update");
        }
    }
}
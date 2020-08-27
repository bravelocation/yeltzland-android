package com.bravelocation.yeltzlandnew;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class YeltzlandWidget extends AppWidgetProvider {

    private AppWidgetManager appWidgetManager;
    private int[] appWidgetIds;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);

        Log.d("YeltzlandWidget", "In onReceive for " + intent.getAction());

        // Update all widgets
        this.updateWidgets(context);
    }

    @Override
    public void onEnabled(Context context)
    {
        Log.d("YeltzlandWidget", "In onEnabled ...");

        // Update all widgets
        this.updateWidgets(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("YeltzlandWidget", "In onUpdate ...");

        this.appWidgetManager = appWidgetManager;
        this.appWidgetIds = appWidgetIds;

        this.updateWidgets(context);
    }

    public static void updateAllWidgets(Context context) {
        Log.d("YeltzlandWidget", "Updating all widgets from static updateAllWidgets() ...");

        // Update the data first, then go and update all the widgets
        TimelineManager.getInstance().fetchLatestData(context, new Runnable() {
            @Override
            public void run() {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                YeltzlandWidget widget = new YeltzlandWidget();
                int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, widget.getClass()));
                widget.onUpdate(context, appWidgetManager, widgetIds);
            }
        });
    }

    private void updateWidgets(Context context) {
        Log.d("YeltzlandWidget", "Updating all widgets ...");

        // Update the data first, then go and update all the widgets
        TimelineManager.getInstance().fetchLatestData(context, new WidgetUpdater(context, this.appWidgetManager, this.appWidgetIds));
    }

    private class WidgetUpdater implements Runnable {
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
}


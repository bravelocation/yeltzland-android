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

    public static void updateAllWidgetsNoDataFetch(Context context) {
        Log.d("YeltzlandWidget", "Updating all widgets from static updateAllWidgetsNoDataFetch() ...");

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        YeltzlandWidget widget = new YeltzlandWidget();
        int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, widget.getClass()));

        WidgetUpdater updater = new WidgetUpdater(context, appWidgetManager, widgetIds);
        updater.run();
    }

    private void updateWidgets(Context context) {
        Log.d("YeltzlandWidget", "Updating all widgets ...");

        // Update the data first, then go and update all the widgets
        TimelineManager.getInstance().fetchLatestData(context, new WidgetUpdater(context, this.appWidgetManager, this.appWidgetIds));
    }
}


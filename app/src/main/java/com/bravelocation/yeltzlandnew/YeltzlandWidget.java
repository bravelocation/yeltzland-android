package com.bravelocation.yeltzlandnew;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class YeltzlandWidget extends AppWidgetProvider {

    public static final String ACTION_AUTO_UPDATE = "AUTO_UPDATE";

    private AppWidgetManager appWidgetManager;
    private int[] appWidgetIds;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        super.onReceive(context, intent);

        Log.d("YeltzlandWidget", "In onReceive for " + intent.getAction());

        if(intent.getAction().equals(ACTION_AUTO_UPDATE))
        {
            // Update all widgets
            this.updateAllWidgets(context);

            // Fetch the latest fixture data ready for next update
            FixtureListDataPump.updateFixtures(context);
        }
    }

    @Override
    public void onEnabled(Context context)
    {
        // Start alarm when widget is installed
        AppWidgetAlarm appWidgetAlarm = new AppWidgetAlarm(context.getApplicationContext());
        appWidgetAlarm.startAlarm();
    }

    @Override
    public void onDisabled(Context context)
    {
        // Stop alarm when widget is removed
        AppWidgetAlarm appWidgetAlarm = new AppWidgetAlarm(context.getApplicationContext());
        appWidgetAlarm.stopAlarm();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("YeltzlandWidget", "In onUpdate ...");

        this.appWidgetManager = appWidgetManager;
        this.appWidgetIds = appWidgetIds;

        this.updateAllWidgets(context);
    }

    private void updateAllWidgets(Context context) {
        Log.d("YeltzlandWidget", "Updating all widgets ...");

        if (this.appWidgetManager != null && this.appWidgetIds != null) {
            // There may be multiple widgets active, so update all of them
            for (int appWidgetId : this.appWidgetIds) {
                // Construct the RemoteViews object
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.yeltzland_widget);

                // Set up the collection
                views.setRemoteAdapter(R.id.widget_list, new Intent(context, WidgetService.class));

                // Instruct the widget manager to update the widget
                this.appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        }
    }
}

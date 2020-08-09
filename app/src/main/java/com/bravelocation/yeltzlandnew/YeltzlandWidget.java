package com.bravelocation.yeltzlandnew;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

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
        this.updateAllWidgets(context);

        // Fetch the latest fixture data ready for next update
        FixtureListDataPump.updateFixtures(context, null);
    }

    @Override
    public void onEnabled(Context context)
    {
        Log.d("YeltzlandWidget", "In onEnabled ...");

        // Update all widgets
        this.updateAllWidgets(context);
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
                Intent intent = new Intent(context, WidgetService.class);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

                views.setRemoteAdapter(R.id.widget_grid, intent);

                // Instruct the widget manager to update the widget
                this.appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        } else {
            Log.d("YeltzlandWidget", "No widgets to update");
        }
    }
}


package com.bravelocation.yeltzlandnew;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
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

        // Update the data first
        TimelineManager.getInstance().loadLatestData();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        YeltzlandWidget widget = new YeltzlandWidget();
        int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, widget.getClass()));
        widget.onUpdate(context, appWidgetManager, widgetIds);
    }

    private void updateWidgets(Context context) {
        Log.d("YeltzlandWidget", "Updating all widgets ...");

        if (this.appWidgetManager != null && this.appWidgetIds != null) {
            // Update the data first
            TimelineManager.getInstance().loadLatestData();

            // There may be multiple widgets active, so update all of them
            for (int appWidgetId : this.appWidgetIds) {
                Log.d("YeltzlandWidget", "Updating widget with ID " + appWidgetId);

                // Construct the RemoteViews object
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.yeltzland_widget);

                // Set up the collection
                Intent intent = new Intent(context, WidgetService.class);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                intent.putExtra("Random", Math.random() * 1000); // Add a random integer to stop the Intent being ignored/cached

                views.setRemoteAdapter(R.id.widget_grid, intent);

                // Instruct the widget manager to update the widget
                this.appWidgetManager.updateAppWidget(appWidgetId, views);
            }
        } else {
            Log.d("YeltzlandWidget", "No widgets to update");
        }
    }
}


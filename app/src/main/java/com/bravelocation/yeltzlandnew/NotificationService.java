package com.bravelocation.yeltzlandnew;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.RemoteViews;

public class NotificationService extends NotificationListenerService {

    Context context;

    @Override
    public void onCreate () {
        super.onCreate() ;
        this.context = getApplicationContext() ;
    }

    @Override
    public void onNotificationPosted (StatusBarNotification sbn) {
        Log.d("YeltzlandWidget", "Notification received, updating widget ...");

        // Notification posted, so update the widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        YeltzlandWidget widget = new YeltzlandWidget();

        // Get a list of the widgets that need updating
        int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, widget.getClass()));

        widget.onUpdate(context, appWidgetManager, widgetIds);
    }
}

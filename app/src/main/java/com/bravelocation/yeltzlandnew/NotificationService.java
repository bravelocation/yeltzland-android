package com.bravelocation.yeltzlandnew;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import android.content.Intent;
import android.os.IBinder;

public class NotificationService extends NotificationListenerService {

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d("YeltzlandWidget", "Notification received, updating widget ...");

        // Notification posted, so update the widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        YeltzlandWidget widget = new YeltzlandWidget();

        // Get a list of the widgets that need updating
        int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, widget.getClass()));

        widget.onUpdate(getApplicationContext(), appWidgetManager, widgetIds);
    }
}

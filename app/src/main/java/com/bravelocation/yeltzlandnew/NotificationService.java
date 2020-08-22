package com.bravelocation.yeltzlandnew;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("NotificationService", "Notification received, updating widget ...");

        // Notification posted, so update the widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        YeltzlandWidget widget = new YeltzlandWidget();

        // Get a list of the widgets that need updating
        int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, widget.getClass()));

        widget.onUpdate(getApplicationContext(), appWidgetManager, widgetIds);
    }
}

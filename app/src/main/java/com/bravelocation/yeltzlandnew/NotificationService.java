package com.bravelocation.yeltzlandnew;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationService extends FirebaseMessagingService {

    Context context;

    @Override
    public void onCreate () {
        super.onCreate() ;
        this.context = getApplicationContext();

        Log.d("YeltzlandWidget", "Notification service created");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("YeltzlandWidget", "Notification received, updating widget ...");

        // Notification posted, so update the widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        YeltzlandWidget widget = new YeltzlandWidget();

        // Get a list of the widgets that need updating
        int[] widgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, widget.getClass()));

        widget.onUpdate(context, appWidgetManager, widgetIds);
    }
}

package com.bravelocation.yeltzland;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by John on 01/06/2016.
 */
public class NotificationsManager {

    private static NotificationsManager instance = null;
    private final Context context;

    public static String preferenceName = "pref_gametimetweets";
    protected static String topic = "gametimealerts";

    public static void init(Context context) {
        if(instance == null) {
            instance = new NotificationsManager(context);
        }
    }

    public static void Update() {
        if(instance != null) {
            instance.Register();
        }
    }

    protected NotificationsManager(Context context) {
        Log.d("NotificationsManager", "Initializing");
        this.context = context;

        // Register for notifications on startup
        this.Register();
    }

    protected void Register() {
        // Are game time tweets enabled
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        boolean gameTweetsEnabled = preferences.getBoolean("pref_gametimetweets", false);

        Handler handler = new Handler();
        Runnable updateSubscriptionRunner = null;

        // Change notifications on background thread
        if (gameTweetsEnabled) {
            updateSubscriptionRunner = new Runnable() {
                @Override
                public void run() {
                    FirebaseMessaging.getInstance().subscribeToTopic(NotificationsManager.topic);
                    Log.d("NotificationsManager", "Subscribed for game time alerts");
                }
            };

        } else {
            updateSubscriptionRunner = new Runnable() {
                @Override
                public void run() {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(NotificationsManager.topic);
                    Log.d("NotificationsManager", "Unsubscribed for game time alerts");
                }
            };
        }

        handler.post(updateSubscriptionRunner);
    }
}

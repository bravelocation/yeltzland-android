package com.bravelocation.yeltzlandnew;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class YeltzlandFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        final RemoteMessage.Notification notification = remoteMessage.getNotification();

        // Show notification on UI thread
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), notification.getBody(), Toast.LENGTH_SHORT).show();
            }
        });

        // Update game score and fixtures
        GameScoreDataPump.updateGameScore(getApplicationContext(), null);
        FixtureListDataPump.updateFixtures(getApplicationContext(), null);
    }
}

package com.bravelocation.yeltzlandnew;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import java.util.Arrays;

import io.fabric.sdk.android.Fabric;

public class YeltzlandApplication extends MultiDexApplication
{
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "mOQVcR8FPJPXw2lwncM5bNkSI";
    private static final String TWITTER_SECRET = "wN1q1WPXs48Vc2sv5sm2f1bivwPrdUNDigy7oOpCUl0dpfvxS5";

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Setup Fabric and Twitter

        Fabric fabric = new Fabric.Builder(this)
                    .kits(new Crashlytics(), new Answers())
                    .debuggable(false)
                    .build();

        Fabric.with(fabric);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(authConfig)
                .debug(true)
                .build();
        Twitter.initialize(config);

        // Setup handler for uncaught exceptions.
        Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException (Thread thread, Throwable e)
            {
                handleUncaughtException(thread, e);
            }
        });

        // Initialize the singletons so their instances
        // are bound to the application process.
        this.initSingletons();
    }

    protected void initSingletons()
    {
        // Initialize the instance of NotificationsManager
        NotificationsManager.init(getApplicationContext());

        // Setup the fixtures
        FixtureListDataPump.updateFixtures(getApplicationContext());

        // Setup the game score
        GameScoreDataPump.updateGameScore(getApplicationContext());

        // Setup the locations
        LocationsDataPump.updateLocations(getApplicationContext());
    }

    public void handleUncaughtException (Thread thread, Throwable e)
    {
        Log.d("Unhandled exception", e.toString());
        e.printStackTrace(); // not all Android versions will print the stack trace automatically
        System.exit(1); // kill off the crashed app
    }
}

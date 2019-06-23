package com.bravelocation.yeltzlandnew;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.tweetui.TweetUi;

public class YeltzlandApplication extends MultiDexApplication
{
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = BuildConfig.TwitterKey;
    private static final String TWITTER_SECRET =  BuildConfig.TwitterSecret;

    @Override
    public void onCreate()
    {
        super.onCreate();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);

        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(authConfig)
                .debug(true)
                .build();
        Twitter.initialize(config);

        // Initialise TweetUI on a background thread - see https://twittercommunity.com/t/a-lot-of-anr-after-twitter-sdk-update-to-3-0-0/89701/3
        Thread thread = new Thread() {
            @Override
            public void run() {
                TweetUi.getInstance();
            }
        };
        thread.start();

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
        FixtureListDataPump.updateFixtures(getApplicationContext(), null);

        // Setup the game score
        GameScoreDataPump.updateGameScore(getApplicationContext(), null);

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

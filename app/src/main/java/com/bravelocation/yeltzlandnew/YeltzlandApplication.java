package com.bravelocation.yeltzlandnew;

import androidx.multidex.MultiDexApplication;
import android.util.Log;

public class YeltzlandApplication extends MultiDexApplication
{
    @Override
    public void onCreate()
    {
        super.onCreate();

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
        Log.e("Unhandled exception", e.toString());
        e.printStackTrace(); // not all Android versions will print the stack trace automatically
        System.exit(1); // kill off the crashed app
    }
}

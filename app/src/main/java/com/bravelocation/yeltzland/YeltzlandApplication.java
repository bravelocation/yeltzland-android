package com.bravelocation.yeltzland;

import android.app.Application;
import android.util.Log;

public class YeltzlandApplication extends Application
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
        initSingletons();
    }

    protected void initSingletons()
    {
        // Initialize the instance of NotificationsManager
        NotificationsManager.init(getApplicationContext());

        // Setup the fixtures
        FixtureListDataPump.updateFixtures(getApplicationContext());

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

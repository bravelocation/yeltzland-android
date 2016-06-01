package com.bravelocation.yeltzland;

import android.app.Application;

public class YeltzlandApplication extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        // Initialize the singletons so their instances
        // are bound to the application process.
        initSingletons();
    }

    protected void initSingletons()
    {
        // Initialize the instance of NotificationsManager
        NotificationsManager.init(getApplicationContext());
    }
}

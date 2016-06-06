package com.bravelocation.yeltzland;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "mOQVcR8FPJPXw2lwncM5bNkSI";
    private static final String TWITTER_SECRET = "wN1q1WPXs48Vc2sv5sm2f1bivwPrdUNDigy7oOpCUl0dpfvxS5";

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);

        Fabric fabric = null;

        if (BuildConfig.DEBUG) {
            Log.d("Yeltzland", "DEBUG mode");
            fabric = new Fabric.Builder(this)
                    .kits(new Twitter(authConfig))
                    .debuggable(true)
                    .build();
        } else {
            Log.d("Yeltzland", "RELEASE mode");
            fabric = new Fabric.Builder(this)
                    .kits(new Crashlytics(), new Twitter(authConfig))
                    .debuggable(false)
                    .build();
        }

        Fabric.with(fabric);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}

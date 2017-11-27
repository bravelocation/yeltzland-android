package com.bravelocation.yeltzlandnew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Are we passing in a tab from shortcut
        int incomingPosition = getIntent().getIntExtra("incomingPosition", -1);
        if (incomingPosition >= 0) {
            // Save selected tab
            SharedPreferences settings = getSharedPreferences(MainActivity.LAST_TAB_PREF_FILE, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(MainActivity.LAST_TAB_PREF_NAME, incomingPosition);
            editor.commit();
        }

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

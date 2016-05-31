package com.bravelocation.yeltzland;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SettingsActivity extends Activity {
    private Typeface textFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        this.textFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "american_typewriter_regular.ttf");

        super.onPostCreate(savedInstanceState);

        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();

        Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.getNavigationIcon().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.white), PorterDuff.Mode.MULTIPLY);

        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        title.setTypeface(this.textFont);

        root.addView(toolbar, 0); // insert at top
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
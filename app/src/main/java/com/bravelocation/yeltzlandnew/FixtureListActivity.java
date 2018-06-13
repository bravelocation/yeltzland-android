package com.bravelocation.yeltzlandnew;

import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class FixtureListActivity extends AppCompatActivity {
    private Typeface textFont;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    LinkedHashMap<String, List<FixtureListDataItem>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixture_list);

        // Add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        View view = this.findViewById(android.R.id.content);

        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        expandableListDetail = FixtureListDataPump.getData();

        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new FixtureListAdapter(getApplicationContext(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        // Expand all sections initially
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("MMMM yyyy", Locale.UK);
        String currentMonth = simpleDateFormat.format(new Date());
        int currentMonthPosition = -1;

        int count = this.expandableListAdapter.getGroupCount();
        for (int position = 0; position < count; position++) {
            String month = (String) this.expandableListAdapter.getGroup(position);
            if (month.contentEquals(currentMonth)) {
                currentMonthPosition = position;
            }

            this.expandableListView.expandGroup(position);
        }

        // Move to current month if set
        if (currentMonthPosition > 0) {
            this.expandableListView.setSelectedGroup(currentMonthPosition);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fixtures_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        } else if (item.getItemId() == R.id.action_reload) {
            Log.d("FixtureListActivity", "Reloading fixtures ...");
            FixtureListDataPump.updateFixtures(getBaseContext(), new FixtureUpdateHandler(this));
        }

        return super.onOptionsItemSelected(item);
    }


    private class FixtureUpdateHandler extends Handler {
        private FixtureListActivity activity;

        FixtureUpdateHandler(FixtureListActivity activity) {
            this.activity = activity;
        }

        public void handleMessage(Message msg) {
            Log.d("FixtureUpdateHandler", "Handling fixture update");

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((FixtureListAdapter) activity.expandableListAdapter).notifyDataSetChanged();
                }
            });
        }
    }
}

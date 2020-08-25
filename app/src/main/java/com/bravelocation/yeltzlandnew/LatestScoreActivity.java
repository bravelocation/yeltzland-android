package com.bravelocation.yeltzlandnew;

import android.os.Handler;
import android.os.Message;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class LatestScoreActivity extends AppCompatActivity {
    private final int RELOAD_INTERVAL = 1000 * 60;  // Refresh once a minute when activity is active
    private LatestScoreUpdateHandler refreshHandler;
    private Timer refreshTimer;

    private TeamLogoManager logoManager;

    private ImageView teamLogoImageView;
    private TextView homeOrAwayTextView;
    private TextView bestGuessTextView;
    private TextView opponentTextView;
    private TextView scoreTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_score);

        // Add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.logoManager = new TeamLogoManager();

        View view = this.findViewById(android.R.id.content);

        this.teamLogoImageView = view.findViewById(R.id.team_logo);
        this.homeOrAwayTextView = view.findViewById(R.id.homeOrAway);
        this.bestGuessTextView = view.findViewById(R.id.best_guess);
        this.opponentTextView = view.findViewById(R.id.opponent_name);
        this.scoreTextView = view.findViewById(R.id.score);

        // Update the UI
        this.updateScoreUI();

        // Setup the refresh handler and timer
        this.refreshHandler = new LatestScoreUpdateHandler(this);
    }

    @Override
    public void onResume() {
        // Setup the timer activity when the activity is shown
        super.onResume();

        Log.d("LatestScoreUpdate", "Setting up update timer ...");
        this.refreshTimer = new Timer();
        this.refreshTimer.schedule(new RefreshTimerTask(this.refreshHandler), RELOAD_INTERVAL, RELOAD_INTERVAL);
    }

    @Override
    public void onPause() {
        // Stop the updates when the activity isn't active
        super.onPause();

        Log.d("LatestScoreUpdate", "Cancelling update timer ...");
        this.refreshTimer.cancel();
        this.refreshTimer.purge();
    }

    public void updateScoreUI() {
        // Populate the data
        FixtureListDataItem latestScoreFixture = GameScoreDataPump.getLatestScore();
        boolean inPlay = false;

        if (GameScoreDataPump.IsGameScoreForLatestGame()) {
            inPlay = true;
        }

        if (latestScoreFixture != null) {
            this.logoManager.LoadTeamImageIntoView(this.getBaseContext(), latestScoreFixture.opponent, this.teamLogoImageView);

            if (latestScoreFixture.home) {
                this.homeOrAwayTextView.setText("vs");
            } else {
                this.homeOrAwayTextView.setText("at");
            }

            this.opponentTextView.setText(latestScoreFixture.opponent);

            if (latestScoreFixture.teamScore == null || latestScoreFixture.opponentScore == null) {
                this.scoreTextView.setText(inPlay ? "0-0*" : latestScoreFixture.fullKickoffTime());
                this.scoreTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.matchDraw));
            } else if (inPlay) {
                this.scoreTextView.setText(String.format(" %d-%d*", latestScoreFixture.teamScore, latestScoreFixture.opponentScore));
                this.scoreTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.matchDraw));
            } else {
                // Result, so set result and color
                String result = "";

                if (latestScoreFixture.teamScore > latestScoreFixture.opponentScore) {
                    result = "W";
                    this.scoreTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.matchWin));
                } else if (latestScoreFixture.teamScore < latestScoreFixture.opponentScore) {
                    result = "L";
                    this.scoreTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.matchLose));
                } else {
                    result = "D";
                    this.scoreTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.matchDraw));
                }

                this.scoreTextView.setText(String.format("%s %d-%d", result, latestScoreFixture.teamScore, latestScoreFixture.opponentScore));
            }

            if (inPlay) {
                this.bestGuessTextView.setVisibility(View.VISIBLE);
            } else {
                this.bestGuessTextView.setVisibility(View.INVISIBLE);
            }
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
            Log.d("LatestScoreActivity", "Reloading latest score ...");
            GameScoreDataPump.updateGameScore(getBaseContext(), this.refreshHandler);
        }

        return super.onOptionsItemSelected(item);
    }


    private static class LatestScoreUpdateHandler implements Runnable {
        private LatestScoreActivity activity;

        LatestScoreUpdateHandler(LatestScoreActivity activity) {
            this.activity = activity;
        }

        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    activity.updateScoreUI();
                }
            });
        }
    }

    private class RefreshTimerTask extends TimerTask {
        private LatestScoreUpdateHandler refreshHandler;

        RefreshTimerTask(LatestScoreUpdateHandler refreshHandler) {this.refreshHandler = refreshHandler; }

        public void run() {
            Log.d("LatestScoreUpdate", "Running update timer ...");
            GameScoreDataPump.updateGameScore(getBaseContext(), this.refreshHandler);
            FixtureListDataPump.updateFixtures(getBaseContext(), this.refreshHandler);
        }
    }
}

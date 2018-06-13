package com.bravelocation.yeltzlandnew;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LatestScoreActivity extends AppCompatActivity {
    private Typeface textFont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_score);

        // Add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        this.textFont = Typeface.createFromAsset(this.getBaseContext().getAssets(), "american_typewriter_regular.ttf");

        // Populate the data
        View view = this.findViewById(android.R.id.content);

        FixtureListDataItem latestScoreFixture = null;
        if (GameScoreDataPump.IsGameScoreForLatestGame()) {
            latestScoreFixture = GameScoreDataPump.getLatestScore();
        } else {
            latestScoreFixture = GameScoreDataPump.getNextFixture();
        }

        if (latestScoreFixture != null) {
            ImageView teamLogoImageView = (ImageView) view.findViewById(R.id.team_logo);
            TeamLogoManager logoManager = new TeamLogoManager();
            logoManager.LoadTeamImageIntoView(latestScoreFixture.opponent, teamLogoImageView);

            TextView homeOrAwayTextView = (TextView) view.findViewById(R.id.homeOrAway);
            if (latestScoreFixture.home) {
                homeOrAwayTextView.setText("vs");
            } else {
                homeOrAwayTextView.setText("at");
            }

            TextView opponentTextView = (TextView) view.findViewById(R.id.opponent_name);
            opponentTextView.setText(latestScoreFixture.opponent);

            TextView scoreTextView = (TextView) view.findViewById(R.id.score);
            if (latestScoreFixture.teamScore == null || latestScoreFixture.opponentScore == null) {
                scoreTextView.setText("TBD");
            } else {
                scoreTextView.setText(String.format("%d-%d", latestScoreFixture.teamScore, latestScoreFixture.opponentScore));
            }
        }

        TextView bestGuessTextView = (TextView) view.findViewById(R.id.best_guess);
        bestGuessTextView.setTypeface(this.textFont);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}

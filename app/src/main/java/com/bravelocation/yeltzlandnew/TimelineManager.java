package com.bravelocation.yeltzlandnew;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

class TimelineManager {
    private TimelineDataItem lastGame;
    private TimelineDataItem currentScore;
    private List<TimelineDataItem> nextGames;

    // Made singleton to make updateable during widget lifecycle
    private static TimelineManager instance = null;

    public static TimelineManager getInstance() {
        if (instance == null) {
            instance = new TimelineManager();
        }
        return instance;
    }

    private TimelineManager() {
        this.loadLatestData();
    }

    public List<TimelineDataItem> timelineEntries() {
        TimelineDataItem firstEntry = null;
        TimelineDataItem secondEntry = null;

        // 1. Get last game, put it into slot 1
        firstEntry = this.lastGame;

        // 2. If current game is in progress, put it into slot one
        if (this.currentScore != null) {
            firstEntry = this.currentScore;
            Log.d("TimelineManager", "Added current score in first entry");
        }

        // 3. Get next fixture
        if (this.nextGames.size() > 0) {
            TimelineDataItem nextGame = this.nextGames.get(0);

            Log.d("TimelineManager", "First next game is against " + nextGame.opponent + " " + nextGame.fixtureDate.toString());

            // 1. If it is not the current game
            if (this.currentScore == null || nextGame.fixtureDate.toString().compareTo(this.currentScore.fixtureDate.toString()) != 0) {
                Log.d("TimelineManager", "Next game is not the current score");

                // If the last game was yesterday or today, put it into slot two, otherwise slot one
                if (this.lastGame != null) {
                    if (daysSinceResult(this.lastGame) <= 1) {
                        secondEntry = nextGame;
                        Log.d("TimelineManager", "Second entry is next game");
                    } else {
                        firstEntry = nextGame;
                        Log.d("TimelineManager", "First entry is next game");
                    }
                } else {
                    firstEntry = nextGame;
                    Log.d("TimelineManager", "First entry is next game");
                }
            }
        }

        // 4. Fill up the remaining slots with subsequent fixtures
        if (firstEntry == null) {
            if (this.nextGames.size() > 0) {
                firstEntry = this.nextGames.get(0);
                Log.d("TimelineManager", "First entry is next game");
            }
            if (this.nextGames.size() > 1) {
                secondEntry = this.nextGames.get(1);
                Log.d("TimelineManager", "Second entry is next but one game");
            }
        } else if (secondEntry == null) {
            if (this.nextGames.size() > 1) {
                secondEntry = this.nextGames.get(1);
                Log.d("TimelineManager", "Second entry is next but one game");
            }
        }

        List<TimelineDataItem> entries = new ArrayList<TimelineDataItem>();
        if (firstEntry != null) {
            entries.add(firstEntry);
        }
        if (secondEntry != null) {
            entries.add(secondEntry);
        }

        return entries;
    }

    public void loadLatestData() {
        Log.d("TimelineManager", "Loading latest data ...");

        // Load current score
        this.currentScore = null;

        if (GameScoreDataPump.IsGameScoreForLatestGame()) {
            FixtureListDataItem currentFixture = GameScoreDataPump.getLatestScore();
            this.currentScore = new TimelineDataItem(
                    currentFixture.fixtureDate,
                    currentFixture.opponent,
                    currentFixture.home,
                    currentFixture.teamScore,
                    currentFixture.opponentScore,
                    TimelineDataItem.TimelineFixtureStatus.inProgress);
            Log.d("TimelineManager", "Current score - " + this.currentScore.opponent);
        }

        // Load last game details
        this.lastGame = null;

        List<FixtureListDataItem> lastResults = FixtureListDataPump.getLastResults(1);
        if (lastResults.size() > 0) {
            FixtureListDataItem lastResult = lastResults.get(0);
            this.lastGame = new TimelineDataItem(
                    lastResult.fixtureDate,
                    lastResult.opponent,
                    lastResult.home,
                    lastResult.teamScore,
                    lastResult.opponentScore,
                    TimelineDataItem.TimelineFixtureStatus.result);
            Log.d("TimelineManager", "Last game - " + this.lastGame.opponent);
        }

        // Get next 2 fixtures
        this.nextGames = new ArrayList<TimelineDataItem>();

        List<FixtureListDataItem> nextFixtures = FixtureListDataPump.getNextFixtures(2);
        for (int i = 0; i < nextFixtures.size(); i++) {
            FixtureListDataItem nextFixture = nextFixtures.get(i);

            TimelineDataItem nextTimelineEntry = new TimelineDataItem(
                    nextFixture.fixtureDate,
                    nextFixture.opponent,
                    nextFixture.home,
                    TimelineDataItem.TimelineFixtureStatus.fixture);

            this.nextGames.add(nextTimelineEntry);
        }

        Log.d("TimelineManager", "Latest data loaded");
    }

    public void fetchLatestData(Context context, Runnable completion) {
        FixtureListDataPump.refreshFixturesFromServer(context, new Runnable() {
            @Override
            public void run() {
                GameScoreDataPump.refreshFixturesFromServer(context, new Runnable() {
                    @Override
                    public void run() {
                        TimelineManager.getInstance().loadLatestData();
                        if (completion != null) {
                            completion.run();
                        }
                    }
                });
            }
        });
    }

    private long daysSinceResult(TimelineDataItem result) {

        Calendar cal = new GregorianCalendar();
        Date now = new Date();

        cal.setTime(now);
        int currentDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
        int currentYear = cal.get(Calendar.YEAR);
        long currentDay = (currentYear * 365) + currentDayOfYear;

        cal.setTime(result.fixtureDate);
        int fixtureDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
        int fixtureYear = cal.get(Calendar.YEAR);
        long fixtureDay = (fixtureYear * 365) + fixtureDayOfYear;

        Log.d("TimelineManager", "Days since result: " + (currentDay - fixtureDay));
        return currentDay - fixtureDay;
    }
}

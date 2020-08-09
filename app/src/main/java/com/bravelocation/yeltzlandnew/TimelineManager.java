package com.bravelocation.yeltzlandnew;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

class TimelineManager {
    private TimelineDataItem lastGame;
    private TimelineDataItem currentScore;
    private List<TimelineDataItem> nextGames;

    public TimelineManager() {
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
        }

        // 3. Get next fixture
        if (this.nextGames.size() > 0) {
            TimelineDataItem nextGame = this.nextGames.get(0);

            // 1. If it is not the current game
            if (nextGame != this.currentScore) {
                // If the last game was yesterday or today, put it into slot two, otherwise slot one
                if (this.lastGame != null) {
                    if (daysSinceResult(this.lastGame) <= 1) {
                        secondEntry = nextGame;
                    } else {
                        firstEntry = nextGame;
                    }
                } else {
                    firstEntry = nextGame;
                }
            }
        }

        // 4. Fill up the remaining slots with subsequent fixtures
        if (firstEntry == null) {
            if (this.nextGames.size() > 0) {
                firstEntry = this.nextGames.get(0);
            }
            if (this.nextGames.size() > 1) {
                secondEntry = this.nextGames.get(1);
            }
        } else if (secondEntry == null) {
            if (this.nextGames.size() > 1) {
                secondEntry = this.nextGames.get(1);
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
    }

    private long daysSinceResult(TimelineDataItem result) {
        Date now = new Date();
        long diff = now.getTime() - result.fixtureDate.getTime();
        return (diff / (1000*60*60*24));
    }
}

package com.bravelocation.yeltzland;

import java.util.Date;

/**
 * Created by John on 21/06/2016.
 */
public class FixtureListDataItem {
    public Date fixtureDate;
    public String opponent;
    public boolean home;
    public Integer teamScore;
    public Integer opponentScore;

    public FixtureListDataItem(Date fixtureDate, String opponent, boolean home) {
        this.fixtureDate = fixtureDate;
        this.opponent = opponent;
        this.home = home;
    }

    public FixtureListDataItem(Date fixtureDate, String opponent, boolean home, Integer teamScore, Integer opponentScore) {
        this.fixtureDate = fixtureDate;
        this.opponent = opponent;
        this.home = home;
        this.teamScore = teamScore;
        this.opponentScore = opponentScore;
    }

    public String kickoffTime() {
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("EEE dd");
        return simpleDateFormat.format(this.fixtureDate);
    }

    public String fixtureMonth() {
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("MMMM yyyy");
        return simpleDateFormat.format(this.fixtureDate);
    }

    public String monthKey() {
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyyMM");
        return simpleDateFormat.format(this.fixtureDate);
    }

    public String displayOpponent() {
        return this.home ? this.opponent.toUpperCase() : this.opponent;
    }

    public String score() {
        if (this.teamScore == null || this.opponentScore == null) {
            return "";
        }

        String result = "";
        if (this.teamScore > this.opponentScore) {
            result = "W";
        } else if (this.teamScore < this.opponentScore) {
            result = "L";
        } else {
            result = "D";
        }

        return result + " " + this.teamScore.toString() + "-" + this.opponentScore.toString();
    }

    public String details() {
        String score = this.score();

        if (score.length() > 0) {
            return score;
        }

        return this.kickoffTime();
    }
}

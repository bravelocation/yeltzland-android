package com.bravelocation.yeltzlandnew;

import java.util.Date;
import java.util.Locale;

public class FixtureListDataItem implements Comparable<FixtureListDataItem> {
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

    public String fullKickoffTime() {
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("EEE dd MMM", Locale.UK);
        return simpleDateFormat.format(this.fixtureDate);
    }

    public String kickoffTime() {
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("EEE dd", Locale.UK);
        return simpleDateFormat.format(this.fixtureDate);
    }

    public String fixtureMonth() {
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("MMMM yyyy", Locale.UK);
        return simpleDateFormat.format(this.fixtureDate);
    }

    public String monthKey() {
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyyMM", Locale.UK);
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

    public String fullDetails() {
        String score = this.score();

        if (score.length() > 0) {
            return score;
        }

        return this.fullKickoffTime();
    }

    @Override
    public int compareTo(FixtureListDataItem o) {
        if (o == null) {
            return -1;
        }

        return this.fixtureDate.compareTo(o.fixtureDate);
    }

    public boolean equals(FixtureListDataItem obj) {
        if (this.opponent.equals(obj.opponent) == false) {
            return false;
        }

        if (this.home != obj.home) {
            return false;
        }


        if (DateHelper.dayNumber(this.fixtureDate) != DateHelper.dayNumber(obj.fixtureDate)) {
            return false;
        }


        if (DateHelper.hourNumber(this.fixtureDate) != DateHelper.hourNumber(obj.fixtureDate)) {
            return false;
        }

        return true;
    }
}

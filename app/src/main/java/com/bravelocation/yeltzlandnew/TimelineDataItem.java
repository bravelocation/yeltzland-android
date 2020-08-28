package com.bravelocation.yeltzlandnew;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class TimelineDataItem implements Comparable<TimelineDataItem>  {

    public enum TimelineFixtureStatus {
        result,
        inProgress,
        fixture
    }

    public enum TimelineFixtureResult {
        win,
        draw,
        lose,
        notFinished
    }

    public Date fixtureDate;
    public String opponent;
    public boolean home;
    public Integer teamScore;
    public Integer opponentScore;
    public TimelineFixtureStatus status;

    public TimelineDataItem(Date fixtureDate, String opponent, boolean home, TimelineFixtureStatus status) {
        this.fixtureDate = fixtureDate;
        this.opponent = opponent;
        this.home = home;
        this.status = status;
    }

    public TimelineDataItem(Date fixtureDate, String opponent, boolean home, Integer teamScore, Integer opponentScore, TimelineFixtureStatus status) {
        this.fixtureDate = fixtureDate;
        this.opponent = opponent;
        this.home = home;
        this.teamScore = teamScore;
        this.opponentScore = opponentScore;
        this.status = status;
    }

    public TimelineFixtureResult result() {
        if (this.teamScore == null || this.opponentScore == null) {
            return TimelineFixtureResult.notFinished;
        }

        if (this.teamScore > this.opponentScore) {
            return TimelineFixtureResult.win;
        } else if (this.teamScore == this.opponentScore) {
            return TimelineFixtureResult.draw;
        } else {
            return TimelineFixtureResult.lose;
        }
    }

    public String score() {
        if (this.teamScore == null || this.opponentScore == null) {
            return "0-0*";
        }

        String inProgressSuffix = "";
        if (this.status == TimelineFixtureStatus.inProgress) {
            inProgressSuffix = "*";
        }

        return this.teamScore.toString() + "-" + this.opponentScore.toString() + inProgressSuffix;
    }

    public String kickoffTime() {
        if (this.isGameToday()) {
            Log.d("TimelineDataItem", "Kickoff vs " + this.opponent + " is today");
            java.text.SimpleDateFormat hourDateFormat = new java.text.SimpleDateFormat("HH:mm", Locale.UK);
            return hourDateFormat.format(this.fixtureDate);
        }

        Log.d("TimelineDataItem", "Kickoff vs " + this.opponent + " is NOT today");
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("EEE dd MMM HH:mm", Locale.UK);
        return simpleDateFormat.format(this.fixtureDate);
    }

    public String opponentPlusHomeAway() {
        return this.home ? this.opponent.toUpperCase() + " (H)" : this.opponent + " (A)";
    }

    public boolean isGameToday() {
        Calendar cal = new GregorianCalendar();
        Date now = new Date();

        cal.setTime(now);
        int currentDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
        int currentYear = cal.get(Calendar.YEAR);
        long currentDay = (currentYear * 365) + currentDayOfYear;

        cal.setTime(this.fixtureDate);
        int fixtureDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
        int fixtureYear = cal.get(Calendar.YEAR);
        long fixtureDay = (fixtureYear * 365) + fixtureDayOfYear;

        return currentDay == fixtureDay;
    }

    @Override
    public int compareTo(TimelineDataItem o) {
        if (o == null) {
            return -1;
        }

        return this.fixtureDate.compareTo(o.fixtureDate);
    }
}

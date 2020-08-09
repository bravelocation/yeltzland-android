package com.bravelocation.yeltzlandnew;

import java.util.Calendar;
import java.util.Date;
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
            return "";
        }

        String inProgressSuffix = "";
        if (this.status == TimelineFixtureStatus.inProgress) {
            inProgressSuffix = "*";
        }

        return this.teamScore.toString() + "-" + this.opponentScore.toString() + inProgressSuffix;
    }

    public String kickoffTime() {
        if (TimelineDataItem.isToday(this.fixtureDate)) {
            java.text.SimpleDateFormat hourDateFormat = new java.text.SimpleDateFormat("HH:mm", Locale.UK);
            return hourDateFormat.format(this.fixtureDate);
        }
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("EEE dd MMM HH:mm", Locale.UK);
        return simpleDateFormat.format(this.fixtureDate);
    }

    public String opponentPlusHomeAway() {
        return this.home ? this.opponent.toUpperCase() + " (H)" : this.opponent + " (A)";
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }

    @Override
    public int compareTo(TimelineDataItem o) {
        if (o == null) {
            return -1;
        }

        return this.fixtureDate.compareTo(o.fixtureDate);
    }
}

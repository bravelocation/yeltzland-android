package com.bravelocation.yeltzlandnew;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

class DateHelper {
    public static long dayNumber(Date date) {

        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        int fixtureDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
        int fixtureYear = cal.get(Calendar.YEAR);
        return (fixtureYear * 365) + fixtureDayOfYear;
    }

    public static long hourNumber(Date date) {

        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal.get(Calendar.HOUR);
    }
}

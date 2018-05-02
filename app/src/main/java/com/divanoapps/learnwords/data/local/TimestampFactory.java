package com.divanoapps.learnwords.data.local;

import java.util.Calendar;
import java.util.TimeZone;

public class TimestampFactory {
    public static long getTimestamp() {
        return Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis();
    }
}

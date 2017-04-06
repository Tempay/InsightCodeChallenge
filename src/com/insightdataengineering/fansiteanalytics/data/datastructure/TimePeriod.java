package com.insightdataengineering.fansiteanalytics.data.datastructure;

import java.util.Calendar;

/**
 * Created by Tempay on 4/5/2017.
 */
public class TimePeriod {
    private final long startTime;
    private int frequency;

    public TimePeriod(long startTime, int frequency) {
        this.startTime = startTime;
        this.frequency = frequency;
    }

    public long getStartTime() {
        return startTime;
    }

    public int getFrequency() {
        return frequency;
    }

    public void increaseFrequency(int increase) {
        frequency += increase;
    }
}

package com.insightdataengineering.fansiteanalytics.data.datastructure;

/**
 * Created by Tempay on 4/5/2017.
 */
public class TimeEntry {
    private final long time;
    private final int frequency;

    public TimeEntry(long time, int frequency) {
        this.time = time;
        this.frequency = frequency;
    }

    public long getTime() {
        return time;
    }

    public int getFrequency() {
        return frequency;
    }
}

package com.insightdataengineering.fansiteanalytics.data.datastructure;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tempay on 4/5/2017.
 */
public class TimeTable {
    private final Map<Long, Integer> timeEntries;
    private long currentTime;
    private int currentFrequency;

    public TimeTable() {
        this.timeEntries = new HashMap<>();
    }

    public void initialize(long time) {
        this.currentTime = time;
        this.currentFrequency = 0;
    }

    public void add(long time) {
        if (time == currentTime) {
            currentFrequency += 1;
        } else {

            timeEntries.put(currentTime, currentFrequency);

            currentTime = time;
            currentFrequency = 1;
        }
    }

    public void end() {
        timeEntries.put(currentTime, currentFrequency);
    }

    public Map<Long, Integer> getTimeEntries() {
        return timeEntries;
    }
}

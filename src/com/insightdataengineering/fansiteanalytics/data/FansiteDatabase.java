package com.insightdataengineering.fansiteanalytics.data;

import com.insightdataengineering.fansiteanalytics.data.datastructure.TimePeriod;
import com.insightdataengineering.fansiteanalytics.data.datastructure.TimeTable;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by Tempay on 4/4/2017.
 */
public class FansiteDatabase {

    private final TimeTable timeTable;
    private final Map<String, Queue<Long>> failedLoginTable;
    private final Map<String, Integer> resourceAccessTable;
    private final Map<String, Integer> hostAccessTable;

    private final int loginPeriod;
    private long startTime;
    private long endTime;

    public FansiteDatabase() {
        this(20, 60 * 60, 10);
    }

    public FansiteDatabase(int loginPeriod, int busiestPeriodLength, int busiestPeriodFactor) {
        this.timeTable = new TimeTable();
        this.failedLoginTable = new HashMap<>();
        this.resourceAccessTable = new HashMap<>();
        this.hostAccessTable = new HashMap<>();
        this.loginPeriod = loginPeriod;
    }

    public void insertTimeEntry(long time) {
        timeTable.add(time);
        endTime = time;
    }

    public void insertHostAccessEntry(String host) {
        Integer accessedTime = hostAccessTable.getOrDefault(host, 0);
        hostAccessTable.put(host, accessedTime + 1);
    }

    public boolean insertFailedLoginEntry(String host, long time) {
        Queue<Long> failedLoginTimeList = failedLoginTable.getOrDefault(host, new PriorityQueue<>(2));

        // remove failed login info before login period (20s as default) ago and earlier
        while (failedLoginTimeList.peek() != null && failedLoginTimeList.peek() + loginPeriod < time) {
            failedLoginTimeList.poll();
        }

        if (failedLoginTimeList.size() >= 2) {
            failedLoginTable.remove(host); // remove this host in login table and return false (block sign)
            return false;
        } else {
            failedLoginTimeList.offer(time);
            failedLoginTable.put(host, failedLoginTimeList);
            return true;
        }
    }

    public void insertResourceAccessEntry(String resource, int bytes) {
        Integer bandwidth = resourceAccessTable.getOrDefault(resource, 0);
        bandwidth += bytes;
        resourceAccessTable.put(resource, bandwidth);
    }

    public void endLoad() {
        timeTable.end();
    }

    public void setStartTime(long time) {
        this.startTime = time;
        this.timeTable.initialize(time);
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public Map<String, Queue<Long>> getFailedLoginTable() {
        return failedLoginTable;
    }

    public Map<String, Integer> getResourceAccessTable() {
        return resourceAccessTable;
    }

    public Map<String, Integer> getHostAccessTable() {
        return hostAccessTable;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }
}

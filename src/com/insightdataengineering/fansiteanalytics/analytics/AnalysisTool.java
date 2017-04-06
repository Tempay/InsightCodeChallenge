package com.insightdataengineering.fansiteanalytics.analytics;

import com.insightdataengineering.fansiteanalytics.data.DataTool;
import com.insightdataengineering.fansiteanalytics.data.FansiteDatabase;
import com.insightdataengineering.fansiteanalytics.data.datastructure.TimeEntry;
import com.insightdataengineering.fansiteanalytics.data.datastructure.TimePeriod;
import com.insightdataengineering.fansiteanalytics.data.datastructure.TimeTable;

import javax.xml.crypto.Data;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by Tempay on 4/5/2017.
 */
public class AnalysisTool {

    public static void generateMostActiveHosts(int factor, Map<String, Integer> hostTable) {
        Queue<Map.Entry<String, Integer>> top = new PriorityQueue<>(factor, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue() - o2.getValue();
            }
        });

        for (Map.Entry<String, Integer> entry : hostTable.entrySet()) {
            top.offer(entry);
            while (top.size() > factor) {
                top.poll();
            }
        }

        Stack<String> content = new Stack<>();

        while (!top.isEmpty()) {
            Map.Entry<String, Integer> entry = top.poll();
            content.push(String.format("%s,%d\n", entry.getKey(), entry.getValue()));
        }

        writeFile("log_output/hosts.txt", content);
    }

    public static void generateMostBandwidthIntensiveResources(int factor, Map<String, Integer> resourceAccessTable) {
        Queue<Map.Entry<String, Integer>> top = new PriorityQueue<>(factor, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o1.getValue() - o2.getValue();
            }
        });

        for (Map.Entry<String, Integer> entry : resourceAccessTable.entrySet()) {
            top.offer(entry);
            while (top.size() > factor) {
                top.poll();
            }
        }

        Stack<String> content = new Stack<>();

        while (!top.isEmpty()) {
            Map.Entry<String, Integer> entry = top.poll();
            content.push(String.format("%s\n", entry.getKey()));
        }

        writeFile("log_output/resources.txt", content);
    }

    public static void generateBusiestTimePeriod(TimeTable timeTable, long startTime, long endTime) {
        Map<Long, Integer> timeEntries = timeTable.getTimeEntries();

        TimePeriod currentPeriod = new TimePeriod(startTime, timeEntries.getOrDefault(startTime, 0));
        long currentEndTime = startTime + 1;

        while (currentEndTime <= Math.min(endTime, startTime + 3600)) {
            currentPeriod.increaseFrequency(timeEntries.getOrDefault(currentEndTime, 0));
            currentEndTime ++;
        }

        Queue<TimePeriod> busiestPeriods = new PriorityQueue<>(10, new Comparator<TimePeriod>() {
            @Override
            public int compare(TimePeriod o1, TimePeriod o2) {

                if (o1.getFrequency() - o2.getFrequency() == 0) {
                    return (int) (o2.getStartTime() - o1.getStartTime());
                } else {
                    return (int) (o1.getFrequency() - o2.getFrequency());
                }
            }
        });

        busiestPeriods.offer(currentPeriod);

        while (currentPeriod.getStartTime() < endTime) {
            currentPeriod = new TimePeriod(currentPeriod.getStartTime() + 1,
                    currentPeriod.getFrequency() - timeEntries.getOrDefault(currentPeriod.getStartTime(), 0));

            currentPeriod.increaseFrequency(timeEntries.getOrDefault(currentPeriod.getStartTime() + 3600, 0));
            busiestPeriods.offer(currentPeriod);

            while (busiestPeriods.size() > 10) {
                busiestPeriods.poll();
            }
        }

        Stack<String> content = new Stack<>();

        while (!busiestPeriods.isEmpty()) {
            TimePeriod period = busiestPeriods.poll();
            content.push(String.format("%s", DataTool.convertTimePeriodToString(period)));
        }

        writeFile("log_output/hours.txt", content);
    }

    private static void writeFile(String filePath, Stack<String> content) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

            while (!content.isEmpty()) {
                writer.write(content.pop());
                writer.flush();
            }

        } catch (IOException ioe) {
            System.out.println(String.format("Fail to write %s", filePath));
        }
    }

}

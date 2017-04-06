package com.insightdataengineering.fansiteanalytics;

import com.insightdataengineering.fansiteanalytics.analytics.AnalysisTool;
import com.insightdataengineering.fansiteanalytics.data.FansiteDatabase;
import com.insightdataengineering.fansiteanalytics.data.Loader;

/**
 * Created by Tempay on 4/5/2017.
 */
public class Main {
    public static void main(String[] args) {
        Loader loader = new Loader("log_input/log.txt"); // loader with default file paths
        FansiteDatabase database = loader.generateData();

        AnalysisTool.generateBusiestTimePeriod(database.getTimeTable(), database.getStartTime(), database.getEndTime());
        AnalysisTool.generateMostActiveHosts(10, database.getHostAccessTable());
        AnalysisTool.generateMostBandwidthIntensiveResources(10, database.getResourceAccessTable());

    }
}

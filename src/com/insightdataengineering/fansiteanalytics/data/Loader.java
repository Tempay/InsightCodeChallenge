package com.insightdataengineering.fansiteanalytics.data;

import com.insightdataengineering.fansiteanalytics.data.datastructure.ActionType;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tempay on 4/4/2017.
 */
public class Loader {

    private final String filePath;
    private final String blockedFilePath;
    private final int defaultBlockingPeriod;
    private final Map<String, Long> blockedTable; // a table contains blocked host with blocking end time

    public Loader() {
        this("log_input/log.txt", "log_output/blocked.txt");
    }

    public Loader(String filePath) {
        this(filePath, "log_output/blocked.txt");
    }

    public Loader(String filePath, String blockedFilePath) {
        this(filePath, blockedFilePath, 300);
    }

    public Loader(String filePath, String blockedFilePath, int defaultBlockingPeriod) {
        this.filePath = filePath;
        this.blockedFilePath = blockedFilePath;
        this.defaultBlockingPeriod = defaultBlockingPeriod;
        this.blockedTable = new HashMap<>();
    }

    public FansiteDatabase generateData() {
        return generateData(DataTool.reader(filePath), DataTool.writer(blockedFilePath));
    }

    public FansiteDatabase generateData(BufferedReader bufferedReader, Writer blockedWriter) {

        FansiteDatabase database = new FansiteDatabase();

        try {
            String nextLine = bufferedReader.readLine(); // a line in log.txt

            TrafficEntry entry = new TrafficEntry(nextLine);
            database.setStartTime(entry.getTime()); // set initial time
            insertEntry(database, entry);

            if (entry.getType().equals(ActionType.LOGIN_FAILED)) { // failed login entry
                database.insertFailedLoginEntry(entry.getHost(), entry.getTime());
            }

            nextLine = bufferedReader.readLine();

            while (nextLine != null) {
                entry = new TrafficEntry(nextLine);

                insertEntry(database, entry);

                if (blockedTable.containsKey(entry.getHost()) && blockedTable.get(entry.getHost()) >= entry.getTime()) {
                    blockedWriter.write(entry.getEntry() + "\n");
                    blockedWriter.flush();

                } else { // this entry is not in block table or out of blocking period

                    blockedTable.remove(entry.getHost()); // remove the blocked entry if it is present

                    if (entry.getType().equals(ActionType.LOGIN_FAILED)) { // failed login entry
                        if (!database.insertFailedLoginEntry(entry.getHost(), entry.getTime())) { // reached the 3rd time limit
                            blockedTable.put(entry.getHost(), entry.getTime() + defaultBlockingPeriod); // add into block table
                        }
                    }
                }
                nextLine = bufferedReader.readLine(); // read next line (entry)
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("File Path or Name is invalid, please check again!");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        database.endLoad(); // flush last period stored in time table
        return database;
    }

    private void insertEntry(FansiteDatabase database, TrafficEntry entry) {
        database.insertHostAccessEntry(entry.getHost());
        database.insertTimeEntry(entry.getTime());
        database.insertResourceAccessEntry(entry.getAction().getTarget(), entry.getAction().getResponseSize());
    }
}

package com.insightdataengineering.fansiteanalytics.data;

import com.insightdataengineering.fansiteanalytics.data.datastructure.Action;
import com.insightdataengineering.fansiteanalytics.data.datastructure.ActionType;

/**
 * Created by Tempay on 4/5/2017.
 */
public class TrafficEntry {

    private final String entry;
    private final String host;
    private final long time;
    private final Action action;

    public TrafficEntry(String entry) {
        this.entry = entry;
        this.host = DataTool.getHost(entry);
        this.time = DataTool.getTimeInMillis(entry);
        this.action = DataTool.getAction(entry);
    }

    public String getEntry() {
        return entry;
    }

    public String getHost() {
        return host;
    }

    public long getTime() {
        return time;
    }

    public Action getAction() {
        return action;
    }

    public ActionType getType() {
        return action.getType();
    }

}

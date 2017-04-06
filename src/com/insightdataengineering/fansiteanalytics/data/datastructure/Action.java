package com.insightdataengineering.fansiteanalytics.data.datastructure;

/**
 * Created by Tempay on 4/5/2017.
 */

public class Action {
    private final ActionType type;
    private final String target;
    private final int responseCode;
    private final int responseSize;

    public Action(ActionType type, String target, int responseCode, int responseSize) {
        this.type = type;
        this.target = target;
        this.responseCode =  responseCode;
        this.responseSize = responseSize;
    }

    public ActionType getType() {
        return type;
    }

    public String getTarget() {
        return target;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getResponseSize() {
        return responseSize;
    }
}

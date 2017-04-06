package com.insightdataengineering.fansiteanalytics.data;

import com.insightdataengineering.fansiteanalytics.data.datastructure.Action;
import com.insightdataengineering.fansiteanalytics.data.datastructure.ActionType;
import com.insightdataengineering.fansiteanalytics.data.datastructure.TimePeriod;

import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Tempay on 4/5/2017.
 */
public class DataTool {

    /**
     * Read the host address (IP or Domain name) in a given string
     * @param entry a line read from the input data
     * @return host address
     * @throws InputMismatchException when no host is matched
     */
    public static String getHost(String entry) {
        Pattern pattern = Pattern.compile("(.*?)\\s\\-\\s\\-");
        Matcher matcher = pattern.matcher(entry);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new InputMismatchException();
        }
    }

    /**
     * Read time in a given string and return a long number
     * @param entry a line in given data input
     * @return a long number represents the time
     * @throws InputMismatchException when no time was matched in given string
     */
    public static long getTimeInMillis(String entry) {

        Pattern pattern = Pattern.compile("\\[(\\d+)\\/(.*?)\\/(\\d+)\\:(\\d+)\\:(\\d+)\\:(\\d+)\\s\\-");
        Matcher matcher = pattern.matcher(entry);

        if (matcher.find()) {
            int year = Integer.parseInt(matcher.group(3));
            int month = convertMonthToInt(matcher.group(2));
            int date = Integer.parseInt(matcher.group(1));
            int hour = Integer.parseInt(matcher.group(4));
            int minute = Integer.parseInt(matcher.group(5));
            int second = Integer.parseInt(matcher.group(6));

            return new GregorianCalendar(year, month, date, hour, minute, second).getTimeInMillis() / 1000;
        } else {
            throw new InputMismatchException("Time format invalid");
        }
    }

    /**
     * Return action of this request.
     * @param entry a line read from the input data
     * @return an Action
     * @throws InputMismatchException if no action is found
     */
    public static Action getAction(String entry) {
        Action action = null;
        Pattern pattern = Pattern.compile("\"(.*?)\\s(.*?)\"\\s(\\d+)\\s(.*)");
        Matcher matcher = pattern.matcher(entry);
        if (matcher.find()) {
            String[] splitTarget = matcher.group(2).split(" ");
            int responseCode = Integer.parseInt(matcher.group(3));
            int responseSize = 0;

            if (!matcher.group(4).equals("-")) {
                responseSize = Integer.parseInt(matcher.group(4));
            }

            if (matcher.group(1).equals("GET") && responseCode == 200) {
                if (splitTarget[0].equals("/")) {
                    action = new Action(ActionType.GET_DEFAULT, splitTarget[0], responseCode, responseSize);
                } else {
                    action = new Action(ActionType.GET_SUCCEED, splitTarget[0], responseCode, responseSize);
                }
            } else if (splitTarget[0].equals("/login") && responseCode == 401) {
                action = new Action(ActionType.LOGIN_FAILED, splitTarget[0], responseCode, responseSize);
            }  else if (splitTarget[0].equals("/login") && responseCode == 200) {
                action = new Action(ActionType.LOGIN_SUCCEED, splitTarget[0], responseCode, responseSize);
            } else {
                action = new Action(ActionType.OTHER, splitTarget[0], responseCode, responseSize);
            }

            return action;

        } else {
            return new Action(ActionType.OTHER, null, 0, 0); // bad input
        }
    }

    public static BufferedReader reader(String filePath) {
        try {
            File file = new File(filePath);
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader;

        } catch (FileNotFoundException fnfe) {
            System.out.println(String.format("No such file at %s", filePath));
        }

        return null;
    }

    public static String convertTimePeriodToString(TimePeriod timePeriod) {
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(timePeriod.getStartTime() * 1000);

        int year = date.get(Calendar.YEAR);
        int mon = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        int hour = date.get(Calendar.HOUR_OF_DAY);
        int minute = date.get(Calendar.MINUTE);
        int second = date.get(Calendar.SECOND);

        String result = String.format("%s/%s/%d:%s:%s:%s -0400,%d\n", intFormat(day),
                convertMonthToString(mon), year, intFormat(hour), intFormat(minute), intFormat(second), timePeriod.getFrequency());

        return result;

    }

    public static Writer writer(String filePath) {
        try {
            return new BufferedWriter(new FileWriter(filePath));

        } catch (IOException ioe) {
            System.out.println(String.format("Can not write to file at %s", filePath));
        }

        return null;
    }


    /**
     * Convert month from String to integer
     * @param month a string include 3 characters represent a month
     * @return integer from 0 to 11
     */
    private static int convertMonthToInt(String month) {
        switch (month) {
            case "Jan":
                return 0;
            case "Feb":
                return 1;
            case "Mar":
                return 2;
            case "Apr":
                return 3;
            case "May":
                return 4;
            case "Jun":
                return 5;
            case "Jul":
                return 6;
            case "Aug":
                return 7;
            case "Sep":
                return 8;
            case "Oct":
                return 9;
            case "Nov":
                return 10;
            case "Dec":
                return 11;
            default:
                return 0;
        }
    }

    /**
     * Convert month from integer to String
     * @param month a string include 3 characters represent a month
     * @return integer from 0 to 11
     */
    private static String convertMonthToString(int month) {
        switch (month) {
            case 0:
                return "Jan";
            case 1:
                return "Feb";
            case 2:
                return "Mar";
            case 3:
                return "Apr";
            case 4:
                return "May";
            case 5:
                return "Jun";
            case 6:
                return "Jul";
            case 7:
                return "Aug";
            case 8:
                return "Sep";
            case 9:
                return "Oct";
            case 10:
                return "Nov";
            case 11:
                return "Dec";
            default:
                return "Jan";
        }
    }

    private static String intFormat(int d) {
        if (d < 10) {
            return String.format("0%d", d);
        } else {
            return String.format("%d", d);
        }
    }

}

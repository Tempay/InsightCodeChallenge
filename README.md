# Fan Site Analytics

## Introduction

In this [code challenge](https://github.com/InsightDataScience/fansite-analytics-challenge), I implemented all 4 required features by Java. Also, I added some flexibilities into this implementation  so that we can define the input/output path and some factors (list more than 10 hosts in output file, etc)

## Implementations

### Data Structure

In my implementation, the basic data structure is a 'Fansitedatabase' which includes 4 major instance:

- TimeTable: a data structure stores all information related to time, 3 instances:
    - a Map stores all time entries:  key: time in second / value: frequency (number of accesses)
    - current Time: current time at input stream
    - current frequency: total accesses at current time

- Failed Login Table: record failed login information

- Resource Access Table: record access times for each Resource

- Host Access Table: record activate times for each Host (IP/Domain)

Also, there are some other data structures used to complement the above structures

### Features' implementation

- Feature 1: Using a Priority Queue to get the top 10 (or given number, this is an add on feature) hosts from Host Access Table

- Feature 2: Using a Priority Queue to get the top 10 (or given number) resources by bandwidth consuming from Resource Access Table

- Feature 3: Set up a window with size 3600 (60 min) and dynamically move the window from start time to end time, for each period, insert it into a fixed sized Priority Queue (size default as 10).

- Feature 4: Implemented it at loading process. Each time when we get a Failed Login access, we try to insert it into FansiteDatabase's failedLoginTable, by calling the insert method (this method will determine whether this host reached 3 times limit), if we get a return value as FALSE, then we know this host reached time limit and we add it to a blocking table with a period (300s as default). For each input, we will first check the blocking table before taking other actions.

## Summary

Due to personal schedule, I only have a very limited time to finish this project, thus the project structure and code style have not been optimized yet. (Some code might not separated and encapsulate into meaningful method/class). Also, some redundant codes are not deleted.

This implementation added some flexibilities and can also add more and more features like HTTP Method statistic (POST/GET).  

### **If you have any difficulty accessing or running this project, please contact me via tianpei@nyu.edu, thanks!**

#!/bin/bash
cd src
javac com/insightdataengineering/fansiteanaly/*.java
cd ..
java -cp src com.insightdataengineering.fansiteanalytics.Main

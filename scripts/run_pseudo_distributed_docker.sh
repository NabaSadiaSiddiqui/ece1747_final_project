#!/bin/bash

# Start Hadoop
$HADOOP_HOME/sbin/start-dfs.sh
$HADOOP_HOME/sbin/start-yarn.sh

# Create input directory in HDFS (/var/app/hadoop/data) and put a file there
hadoop fs -mkdir -p /var/app/hadoop/data/input
hadoop fs -put $HADOOP_HOME/LICENSE.txt /var/app/hadoop/data/input

# Run MapReduce job
hadoop jar app.jar App /var/app/hadoop/data/input/* /var/app/hadoop/data/*

# Clean up .class files
rm *.class

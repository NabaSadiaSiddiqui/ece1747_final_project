#!/bin/bash

# AUTHOR: Vipin Bakshi
# DATE:   20161203
# PURPOSE:This script compiles the Java program using Hadoop and binds with all the dependencies. Then it runs the Hadoop application in Pseude-Distributed mode.OB

# Compile using Hadoop and create JAR.
echo "Compiling program..."
hadoop com.sun.tools.javac.Main App.java
jar cf App.jar App*.class

# Run in Pseudo-Distributed Mode.
echo "Starting Hadoop Systems..."
rm -r ~/hadoopinfra/hdfs/datanode/*
hdfs namenode -format
hdfs datanode -format
start-dfs.sh
hadoop fs -mkdir -p ~/hadoopinfra/hdfs/datanode/input
hadoop fs -put ../data/* ~/hadoopinfra/hdfs/datanode/input/
start-yarn.sh

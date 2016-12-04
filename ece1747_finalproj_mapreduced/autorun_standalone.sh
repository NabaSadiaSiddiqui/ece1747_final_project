#!/bin/bash

# AUTHOR: Vipin Bakshi
# DATE:   20161203
# PURPOSE:This script compiles the Java program using Hadoop and binds with all the dependencies. Then it runs the Hadoop application in Pseude-Distributed mode.

# Compile using Hadoop and create JAR.
echo "Compiling program\n"
hadoop com.sun.tools.javac.Main App.java
jar cf App.jar App*.class

# Run in standalone mode.
echo "Running Hadoop Task"
hadoop jar App.jar App ../data/*.csv ../data/output

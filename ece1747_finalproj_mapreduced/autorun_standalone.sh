#!/bin/bash

# AUTHOR: Vipin Bakshi
# DATE:   20161203
# PURPOSE:This script compiles the Java program using Hadoop and binds with all the dependencies. Then it runs the Hadoop application in Pseude-Distributed mode.

# Compile using Hadoop and create JAR.
echo "Compiling program..."
hadoop com.sun.tools.javac.Main *.java
jar cvf App.jar *.class

# Run in standalone mode.
echo "Running Hadoop Task..."
rm -r ../data/outputs
hadoop jar App.jar App.App  ../data/* ../data/outputs/


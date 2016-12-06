#!/bin/bash

# AUTHOR: Vipin Bakshi
# DATE:   20161203
# PURPOSE:This script compiles the Java program using Hadoop and binds with all the dependencies. Then it runs the Hadoop application in Pseude-Distributed mode.

# Compile using Hadoop and create JAR.
echo "Compiling program..."
hadoop com.sun.tools.javac.Main ../ece1747_finalproj_mapreduced/*.java
jar cf ../ece1747_finalproj_mapreduced/App.jar ../ece1747_finalproj_mapreduced/*.class

# Run in standalone mode.
echo "Running Hadoop Task..."
rm -r ../data/outputs
hadoop jar ../ece1747_finalproj_mapreduced/App.jar App  ../data/* ../data/outputs/


#!/bin/bash

# AUTHOR: Vipin Bakshi
# DATE:   20161203
# PURPOSE:This script is called after config_pseudodist.sh and runs the hadoop application.

# Run application assuming that the psedodist Hadoop is running via config_pseudodist
hadoop jar App.jar App ~/hadoopinfra/hdfs/datanode/input/* ~/hadoopinfra/hdfs/datanode/*

#Launch Browser in the end
firefox -url http://localhost:8088

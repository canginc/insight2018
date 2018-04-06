#!/bin/bash
#
# Use this shell script to compile (if necessary) your code and then execute it. Below is an example of what might be found in this file if your program was written in Python
#
#

# SPECIFY FULL PATHS of input timeout, input log, output files
INPUT_TIMEOUT_FILE="./input/inactivity_period.txt"
INPUT_FILE="./input/log.csv" 
OUTPUT_FILE="./output/sessionization.txt" 


javac  ./src/EachLine.java   ./src/Frame.java      ./src/Main.java      ./src/Session.java   

# pass strings arguments to Main  method
java -Dfile.encoding=UTF-8 -classpath ./src   Main	$INPUT_TIMEOUT_FILE	$INPUT_FILE	$OUTPUT_FILE

## do NOT use window command
# "C:\Program Files\Java\jdk-10\bin\java" "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2018.1\lib\idea_rt.jar=61982:C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2018.1\bin" -Dfile.encoding=UTF-8 -classpath C:\Y\_insights\out\production\_insights Main
### OK good

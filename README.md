Although the program passed several test suites, I Apologize for the following performance issue and the lack of test cases because I were handling unexpected emergency repair projects of water-heater-tank at home.

(1) Input arguments
 The run.sh file contains these 3 filenames, which are passed as arguments to the following java program:
	the input timeout file path (log.csv)
	the input file path (log.csv)
	the output sessionization file (sessionization.txt).

	For example 
	# SPECIFY FULL PATHS of input timeout, input log, output files
	INPUT_TIMEOUT_FILE="./input/inactivity_period.txt"
	INPUT_FILE="./input/log.csv" 
	OUTPUT_FILE="./output/sessionization.txt" 

	javac  ./src/EachLine.java   ./src/Frame.java      ./src/Main.java      ./src/Session.java
	# pass strings arguments to Main  method
	java -Dfile.encoding=UTF-8 -classpath ./src   Main	$INPUT_TIMEOUT_FILE	$INPUT_FILE	$OUTPUT_FILE

(2) Efficiency issues:
For each incoming new line of web request in the log.csv file, the program checks the incoming IP address against the LINKEDHASHMAP structure of the current Frame, to verify whether the new incoming IP address should be merged with an existing session in the LINKEDHASHMAP, or whether it should be a new session (due to timeout of previous session with the same ip address).

However, an efficiency issue remain. As the clock advance in time, the program uses linear search method to print out and remove all expired sessions, which would be a performance bottleneck. Due to other unexpected home repair issues, I run out of time to implement an  additional data structure  to print out and to remove all expired sessions while the clock advance forward.

(3) JAVADOC was used to document the code; however, I have not yet complete detailed JUNIT tests.

(1) Input arguments
 In the run.sh file, specify the 3 filenames as the following:
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
For each incoming new line of web request in the log.csv file, the program checks the incoming IP address against the LINKEDHASHMAP structure of the current Frame, to verify whether the new incoming IP address should be merged with an existing session in the LINKEDHASHMAP, or whether it should be a new session (due to timeout).

However, as the clock advance in time, it uses the Linear search method to print out and remove all expired sessions (which would be a performance bottleneck). Due to other unexpected scheduling issues, I run out of time to implement another additional data structure approach to print out and remove all expired sessions while the clock advance forward.


(3) JAVADOC was used to document the code; however, I have not had the time to complete detailed JUNIT tests.


/**
 * 
 * ASSUME that input activity second file name is in UNIX LINUS FORMAT ./input/inactivity_period.txt
 * ASSUME that input file name is in UNIX LINUS FORMAT ./input/log.csv
 * ASSUME that output file name is in UNIX LINUS FORMAT ./output/sessionization.txt
 * 
 *  @author canginc@gmail.com
 *
 *
 */
public class Main {


    public static void main(String[] args) {

	// WINDOWS: not using windows
	//Frame   frameEDGAR = new Frame ( ".\\input\\inactivity_period.txt", ".\\input\\log.csv" , ".\\output\\sessionization.txt" );

	// *****************************************************************
	// LINUX
	//        Frame   frameEDGAR = new Frame ( "./input/inactivity_period.txt", "./input/log.csv" , "./output/sessionization.txt" );
	//
	// **** default arguments strings initializations
	//
	String IN_timeoutFile = "./input/inactivity_period.txt";
	String IN_WebFile =  "./input/log.csv";
	String OUT_LOG= "./output/sessionization.txt" ;
 
	if (args.length==3) {
		//System.out.println("@@@ :) :)  OK read from BASH_args[0]="+ args[0]+ "; BASH_args[1]= "+args[1] +"; BASH_args[2]=" + args[2] );
		IN_timeoutFile = args[0];
		IN_WebFile = args[1];
		OUT_LOG = args[2];
	}
        Frame   frameEDGAR = new Frame ( IN_timeoutFile, IN_WebFile, OUT_LOG );
        frameEDGAR.edgarProject();
    }
}

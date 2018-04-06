import java.io.File;
import java.io.Writer;
import java.io.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.LinkedHashMap;


/**
 * 
 * A Frame consists of maxTimeOut,  file Scanner, indices of relevant Session data;
 * Most importantly, a Frame  edgarProject() function processes thes input files to dynamically  process the  colletion of active Sessions :
 * These active Sessions are stored in the form of  LinkedHashMap<String, Session> , as  LinkedHashMap<ip String, Session object>
 *  @author canginc@gmail.com
 *
 */
public class Frame {

	/**
	 *STATIC INPUTS: maxTimeOutfile; log.csv LOG lg_file; indices  represents "single integer value denoting the period of inactivity (in seconds) that your program should use to identify a user session. The value will range from 1 to 86,400 (i.e., one second to 24 hours)" 
	 */
    static private int maxTimeOut  =10; // INPUT default 10 seconds timeout
    
    /**
     * each line of log.csv input file should contain at least 3 fields: i.e. (IP, date, time)
     */
    static final private int  minNumberDelimitedTokens  =3;

    /**
     * instance of Scanner reading the  log.csv  input file 
     */
    static private Scanner log_Scanner =null;  
    /**
     * instance of BufferedWriter
     */
    static private BufferedWriter Out_Writer =null;

    /**
     * the 6 indices correspond to the COMMA-parsed input line of log.csv LOG file 
     */
    static private int index_ip=0, index_date=1, index_time=2, index_cik, index_acc, index_ext ;  //INPUT  indices of header in lg_file

    /**
     * Delimiter used to parse input log.csv file 
     */
    static private final  String delims = "[,]";

    /**
     * CONSTRUCTOR default 
     * needs the PATH/NAME of the inactivity filename, the input log file, output file
     *  populates all static private variable of the class
     * @param inactivityFilename 	PATH_and_NAME of the # seconds inactivity file
     * @param logFilename			the PATH_and_NAME of input log file
     * @param outFilename			the PATH_and_NAME of the output Filename 
     */ 
    Frame (String inactivityFilename, String  logFilename , String  outFilename  ) {
        // read in maximum timeout value from ./input/inactivity_period.txt  file
        try {
            File inact_file=new File( inactivityFilename );
            Scanner sc = new Scanner(inact_file ,  "UTF-8");
            if (sc.hasNextLine()) {
                maxTimeOut = Integer.parseInt(sc.nextLine()); 
            } //System.out.println("Good maxTimeOut=" + maxTimeOut +"***");
            sc.close();
        } catch (Exception e ) {
            e.printStackTrace();
        }
        try {
            // reading the HEADER of the input log file
            File logFile = new File( logFilename );
            log_Scanner = new Scanner(logFile  ,  "UTF-8");   
            
            /// HEADER of log file: Get the indices of the 3 main fields in HEADER= ip + datetime+ cikAccExt
            String header = "";
            if (  log_Scanner.hasNextLine()) {
                header = log_Scanner.nextLine();
            }
            String[] tokens = header.split(delims);
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].equals("ip")  ){  index_ip = i;}
                if (tokens[i].equals("date") ){ index_date = i; }
                if (tokens[i].equals("time") ){ index_time = i;   }
                if (tokens[i].equals("cik") ){ index_cik =i; }
                if (tokens[i].equals("accession") ){  index_acc =i; }
                if (tokens[i].equals("extention") ){ index_ext =i; }
            }

            // output file    
            Out_Writer = new BufferedWriter( new OutputStreamWriter(  new FileOutputStream(outFilename),  StandardCharsets.UTF_8  )  );
            // wrting strings to Possibly BIG file

        }catch (IOException  IOe ) {
            IOe.printStackTrace();
            System.out.print("Error reading log.csv  IOException **" );
        }catch (Exception e ) {
            e.printStackTrace();
            System.out.print("Error reading log.csv, Cannot read log.csv **" +  e  );
        }
    }
    
    /**
     * Process input log.csv file from non-header second line to last line of input file;
     * Store all ACTIVE SESSIONS in LinkedHashMap LHMap ; keep track of current second and maximum second in LHMap;
     * For each additional current input line, check that it has at least (ip, date, time) required field; if current line makes LHMap expires, then print out the entire frame and insert current line;
     * if current line does not make LHMap expires and TimeStamp advancing, then check and print all expired sessions and then remove expired sessions.
     * if current line does not make LHMap expires and TimeStamp does NOT advance, 
     * then either MERGE current line with LHMap (found pre-exisitng ip found in LHMap) or ADD current line to LHMap (no pre-exisitng ip found in LHMap)
     */
    public  void edgarProject() {
         try { 
        	 // BODY  log.csv Read first line 
            if ( !(log_Scanner.hasNextLine()) ) { System.out.println("## Empty log.csv file with NO line of data"); throw new IOException();   }
            String beginStr = log_Scanner.nextLine();
            String[]  tokens = beginStr.split(delims);
            EachLine firstLine = new EachLine(tokens[index_ip], tokens[index_date], tokens[index_time] ); // tokens[index_cik], tokens[index_acc], tokens[index_ext] );
            Session firstSession = new  Session( firstLine, maxTimeOut );

            // create Linked HASHmap of <key=ip, session>
            LinkedHashMap<String, Session>  frameLHM = new  LinkedHashMap<String, Session> ();
            frameLHM.put(firstSession.get_ip(), firstSession);      //next 2 timestamps  are used to detect large TIME GAP (>maxTimeOut)  between existing frame and new Line
            long prevMaxSeconds = firstLine.get_SecondsDatetime();  //previous Max seconds of frameLHM
            long curSeconds = firstLine.get_SecondsDatetime();      //current timestamp seconds

            /// read each line of log.csv to fill FRAME
            while (log_Scanner.hasNextLine()) {
                String  lineInfo = log_Scanner.nextLine();
                tokens = lineInfo.split(delims);
                if (tokens.length  < minNumberDelimitedTokens  ) { 
               	// if a line of input log file lacks MINIMUM tokens= ip, date, time; cik, accession, extention
                    System.out.print("@@@ line of input file is TOO short: tokens.length = "+ tokens.length +"; too SHORT line =" + lineInfo + ";tokens=" + tokens.toString());
                    continue;
                }
                EachLine curLine = new EachLine( tokens[index_ip], tokens[index_date], tokens[index_time]);  // each current line
                curSeconds  = curLine.get_SecondsDatetime();
                
                //  NOT TIME_OUT FRAME ; NOT TIMEOUT_GAP between existing frameLHM and new_curLine
                //System.out.println(" ### curSeconds=" +curSeconds +" ;prevMaxSeconds= " + prevMaxSeconds +"; diff curSeconds - prevMaxSeconds =" + (curSeconds - prevMaxSeconds) );
                if ( curSeconds - prevMaxSeconds  <= maxTimeOut) {
                	//timestamp CLOCK is advancing
                    if ( curSeconds > prevMaxSeconds ){ //print out  ALL EXPIRED  session(s) in frameLHM; linear search all sessions in
                        printRmvExpiredSessions ( frameLHM,  curLine.get_datetime() ) ;
                    }
                    // either crLine ip EXISTS or NOT_EXISTS in frame
                    Session preexistingSession= (Session) frameLHM.get( curLine.get_ip() ) ; 
                    if (preexistingSession != null){  // found preexisting IP
                        boolean okMerged=  preexistingSession.mergeLine(curLine );
                        assert( okMerged  );
                    }else {     // NOT  foundIP; can NOT find  preexisting IP
                        Session curSn = new  Session( curLine, maxTimeOut );
                        Object shouldbeNull= frameLHM.put( curSn.get_ip(), curSn );
                        assert (shouldbeNull ==null ); 	// because no pre-existing session with curSn.ip
                    }
                    prevMaxSeconds= curLine.get_SecondsDatetime();
                } else {  
                	// TIME_OUT; the entire frame's TIME_OUT; print entire frame, reset frame to empty, add or  crLine into frame
                    //System.out.print("@@@ TIME_OUT; the entire frame's TIME_OUT; ( curSeconds - prevMaxSeconds )=" +( curSeconds - prevMaxSeconds )+"  print entire frame="+ frameLHM);
                    printAllSessions ( frameLHM );  //print entire frame
                    frameLHM.clear();               //  reset frameLHM  to EMPTY
                    frameLHM =  new  LinkedHashMap<String, Session>  ();   // REINITIALIZED the linked hash map to a  BRAND NEW   frame
                    Session curSn = new  Session( curLine, maxTimeOut );
                    frameLHM.put( curSn.get_ip(), curSn ); //adding new Session to empty frameLHM
                    prevMaxSeconds = curLine.get_SecondsDatetime();
                    curSeconds = curLine.get_SecondsDatetime();
                }
            } // finished reading file
            printAllSessions ( frameLHM );  // print all  sessions after end of reading log.csv
            frameLHM.clear();               //  reset frame to empty

            if (Out_Writer != null)
                 Out_Writer.close();
        }catch (IOException ioe ) {
             System.out.print("Error reading log.csv IOException" +  ioe  );
             ioe.printStackTrace();
        } catch (Exception e ) {
            System.out.print("Error reading log.csv, Cannot read log.csv****" +  e  );
            e.printStackTrace();
        } // finally { }
    }//  edgarProject  function

    /**
     * 	printing all sessions in LinkedHashMap
     * 	@param LHM LinkedHashMap<String ip, Session> 
     * @throws IOException
     */
    private static void printAllSessions ( LinkedHashMap<String, Session>  LHM ) throws IOException {
        Iterator<Map.Entry<String, Session>> iterator = LHM.entrySet().iterator();
        Session ssn ;
        while(iterator.hasNext()){
            Map.Entry<String, Session>       entry =   iterator.next();
            ssn =   entry.getValue();

            //System.out.println( ssn  );
            Out_Writer.write( ssn.toString()   );
            Out_Writer.newLine();
        }
    }

    /**
     * printing all EXPIRED sessions in LinkedHashMap, and then remove all EXPIRED sessions from LinkedHashMap
     * unfortunate linear search of all sessions in the Linked Hash Map to see which one is EXPIRED;
     * @param LHM
     * @param currentStrDateTime
     * @throws IOException
     */
    private static void printRmvExpiredSessions ( LinkedHashMap<String, Session>  LHM, String currentStrDateTime ) throws  IOException{
        Iterator<Map.Entry<String, Session>> iterator = LHM.entrySet().iterator();
        
        Session eachSn;
        while(iterator.hasNext()){
            Map.Entry<String, Session>       entry =   iterator.next();
            eachSn =  (Session) entry.getValue() ;

            long idleTime= eachSn.DateTimeDifference(eachSn.get_end_datetime(), currentStrDateTime);

            // print  expired session;  remove expired session
            if (  idleTime > maxTimeOut ) { //EXPIRED session
                //System.out.println (eachSn);    // print  expired session
                Out_Writer.write( eachSn.toString()    );
                Out_Writer.newLine();

                iterator.remove();              //remove expired session
            }   //done printing expired session; done removing expired session
        } //iterator
    }
    
    /**
     * 
     * LinkedHashMap preserves the order of insertion; so getElementByIndex by index will yield the same order of insertion
     * @param LHM
     * @param index
     * @return that SESSION indiced in LHM
     */

    public Object getElementByIndex(LinkedHashMap LHM ,   int index){
        return LHM.get( (LHM.keySet().toArray())[ index ] );
    }
}

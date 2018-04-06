/**
 * 
 * ASSUME that input activity second file name is in UNIX LINUS FORMAT ./input/inactivity_period.txt
 * ASSUME that input file name is in UNIX LINUS FORMAT ./input/log.csv
 * ASSUME that output file name is in UNIX LINUS FORMAT ./output/sessionization.txt
 * 
 *  @author canginc@gmail.com
 *
 */
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Each session contains CONDENSED combination/compilation of many instances of "EachLine" (as formatted in   log.csv)
 * contains ip, start_datetime, end_datetime, DURATIONsec, #web requests, max seconds time out
 * 
 */
public class Session {
    private String ip;
    private String start_datetime;
    private String end_datetime;
    private long durationSec;
    private int NumWebReq;
    private static  int  maxSecTimeOut;

    /**
     * get string of ip
     * @return IP
     */
    public  String get_ip() {  return ip;  }
    /**
     * get string of start_datetime 
     * @return start_datetime
     */
    public  String get_start_datetime() {  return start_datetime;  }
    /**
     * get string of end_datetim
     * @return  end_datetim
     */
    public  String get_end_datetime() {  return end_datetime;  }
    
    /**
     * get string of durationSec
     * @return durationSec
     */
    public  long get_durationSec() {  return durationSec;  }
    
    /**
     * get # webrequests
     * @return # webrequests
     */
    public  int get_NumWebReq() {  return NumWebReq;  }
    /**
     * get maxSecTimeOut
     * @return maxSecTimeOut
     */
    public  int get_maxSecTimeOut () {  return maxSecTimeOut;  }

	/**
	 * need 2 date time strings  
	 * @param cur_datetime
	 * @param newerDT
	 * @return long # seconds differences between cur_datetime & newerDT
	 */
    public long  DateTimeDifference (String cur_datetime , String  newerDT ) {
        //SimpleDateFormat format = new SimpleDateFormat ("MM/dd/yyyy HH:mm:ss");  //excel format
        SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");  // csv comma separated file format  working
        long diff_millisec = 0L;
        try{
            Date d1 = format.parse( cur_datetime );
            Date d2 = format.parse( newerDT  );
            diff_millisec = d2.getTime() - d1.getTime();
        } catch ( Exception e) {
            System.out.println( "@@@@@!!! Exception DateTimeDifference @@@@@@@@@" );
            e.printStackTrace();
        }
         return  (diff_millisec/1000);
    }


    /**
     * CONSTRUCTOR of EachLine, the line input exists in session  for the very first time
     * @param eachLine
     * @param maxSecTimeOutVal
     */
    Session ( EachLine eL,  int  maxSecTimeOutVal ) {
        try{
            ip = eL.get_ip ();
            start_datetime = eL.get_datetime();
            end_datetime  = eL.get_datetime() ;
            durationSec =  1 + DateTimeDifference( start_datetime, end_datetime );
            NumWebReq = 1;
            maxSecTimeOut =maxSecTimeOutVal;
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }

	/**
	 *  Merge Line 
	 *  @param adding EachLine
	 *  @return true only if the new addLn EachLine was merged into this Session; return false if either inactive session or unmatched  ip (of addLn)
	 *  
	 */
    boolean mergeLine ( EachLine addLn   ) {
        long addLn_secDiff_curEnd= this.DateTimeDifference( end_datetime, addLn.get_datetime() );

        //No merge; INACTIVE current session; do NOT merge this new line because this current session is INACTIVE
        if ( this.ip.equals( addLn.get_ip() ) &  addLn_secDiff_curEnd > maxSecTimeOut ) {
             System.out.println( "###  INACTIVE current session; do NOT merge this new line because this current session is INACTIVE;" +addLn );
             return false;
        //MERGE  this new line only if new ipVal is identical to this.ip
        }else  if  ( this.ip.equals( addLn.get_ip() ) & addLn_secDiff_curEnd  <= maxSecTimeOut ) {
            end_datetime = addLn.get_datetime() ;
            durationSec  = 1+ this.DateTimeDifference( start_datetime, addLn.get_datetime() ) ;
            NumWebReq    = 1+ NumWebReq;
            return true;
        } else {
        // DIFFERENT IP:  this.ip different than   addLn.ip
            System.out.println( "### Different IP, do NOT merge this new line because new Line has DIFFERENT IP than session IP;"+ addLn);
            return false;
        }
    }

    /**
     * converting input Session to a string
     * @param output string
     */
    public String toString() {
        return ip + "," + get_start_datetime() + "," + get_end_datetime()   + ","+  get_durationSec()  + "," + get_NumWebReq()  ; //+ "\n" ;
        // return ip + ", start_datetime=" + get_start_datetime() + ", end_datetime=" + end_datetime  + ", durationSec=" + durationSec  + ", NumWebReq=" + NumWebReq ;
   }

}

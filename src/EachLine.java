import java.text.SimpleDateFormat  ;
import java.util.Date;

/**
 * Each instance of this class EachLine corresponds to each line of input of log.csv file.
 * It contains unique IP, datetime, and the 3-parts WebRequestInfo.
 * Since Current requirement assumes that Repeated identical  Webrequests are counted separately,
 * There are 2 constructors: one with  3-parts WebRequestInfo, and the second without 3-parts WebRequestInfo
 * @author canginc@gmail.com
 *
 */
public class EachLine {

    /** ip address
     * Unique identifier
     */
    private String ip;

    /** time request
     * date and time format ASSUMED to be in  SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
     */
    private String date;
    private String time;

    /**
     * WebRequest as Uniquely identify these  3 fields cik, acc, ext;
     */
    private String cik;
    private String acc;
    private String ext;
    
    // Starting methods
    /**
     * Getter of Unique IP string
     * @return  Unique IP
     */
    public  String get_ip (){  return ip;  }
    /**
     * getter of date string
     * @return   date
     */
    public  String get_date(){  return date;  }
    /**
     * getter of time string
     * @return   time
     */
    public  String get_time(){  return time;  }
    /**
     *  Return concatenated date time of this line
     * @return datetime
     */
    public  String get_datetime() {
        return ( date + " "+ time ) ;
    }
    
    /**
     *  Return CIK part of 3 fields WEBrequests: unique web requests are composed of 3 fields cik, acc, ext;
     * @return CIK
     */
    public  String get_cik(){  return cik;  }
    /**
     *  Return ACC part of 3 fields WEBrequests:unique web requests are composed of 3 fields cik, acc, ext;
     * @return ACC
     */
    public  String get_acc(){  return acc;  }
    /**
     *  Return EXT part of 3 fields WEBrequests:unique web requests are composed of 3 fields cik, acc, ext;
     * @return EXT
     */
    public  String get_ext(){  return ext;  }
    
    
    /**
     * Computes and returns the # Seconds equivalent to "concatenated date time string"
     * @return Seconds Representation of datetime
     */
    public  long get_SecondsDatetime() {
        long msc =0;
        Date thisDate;
        try {
            // do not use excel //SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"); // excel format
            SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");  // csv format
            String dt = get_datetime();
            thisDate = format.parse(dt);
            msc = thisDate.getTime();   //milliseconds long
        }catch ( Exception e) {
            e.printStackTrace();
        }
        return ( msc/1000 ) ;
    }

    /**
     *  CONSTRUCTOR of EachLine  using only IP, Date,Time; 3PartsWebReuqest (CIK, ACC, EXT)
     * @param ipVal
     * @param dateVal
     * @param timeVal
     * @param cikVal
     * @param accVal
     * @param extVal
     */
    EachLine (String ipVal, String dateVal, String  timeVal, String cikVal,String accVal, String extVal  ) {
        ip =ipVal;
        date =dateVal;
        time =timeVal;
        cik =cikVal;
        acc =accVal;
        ext=extVal;
    }

    /**
     * CONSTRUCTOR of EachLine using only IP and Date,Time
     * @param ipVal
     * @param dateVal
     * @param timeVal
     */
    EachLine (String ipVal, String dateVal, String  timeVal  ) {
        ip =ipVal;
        date =dateVal;
        time =timeVal;
    }

    /**
     * toString
     * @return String representation of this instance of Line
     */
    public String toString() {
        return "Line object ip=" + get_ip () + ", date=" + get_date()   + ", time=" + get_time()    ;
    }
}

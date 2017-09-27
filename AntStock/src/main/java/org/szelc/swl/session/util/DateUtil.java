
package org.szelc.swl.session.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author ms
 */
public class DateUtil {

    private static DateUtil instance;
    
    private static SimpleDateFormat dateFormatddMMyyyy = new SimpleDateFormat("ddMMyyyy");

    private DateUtil(){

    }
    
    public static Date getDateddMMyyyy(String date){
        try {
            return dateFormatddMMyyyy.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }

    public static DateUtil createInstance(){
        if(instance==null)
            instance = new DateUtil();
        return instance;
    }

    public enum RESULT {

        FIRST_DATE_BEFORE,
        FIRST_DATE_AFTER,
        DATE_EQUAL
    }

     static final String dateFormat = "dd-MM-yy";
     static final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
             
    public  static RESULT compareDate(String date1, String date2, String format) {
        try {
           
            Date d1 = sdf.parse(date1);
            Date d2 = sdf.parse(date2);
            if (d1.before(d2)) {
                return RESULT.FIRST_DATE_BEFORE;
            } else if (d1.after(d2)) {
                return RESULT.FIRST_DATE_AFTER;
            } else {
                return RESULT.DATE_EQUAL;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }
    
    
    
     public  int compareDate(String d1, String d2){
        try {
            //log.info("date1 ["+date1.getTime()+"] date2 ["+date2.getTime()+"]");
            return sdf.parse(d1).compareTo(sdf.parse(d2));
        } catch (ParseException e) {
            e.printStackTrace();
            System.exit(0);
            return 0;
            
        }
        //log.info(date1.compareTo(date2));
//        return date1.compareTo(date2);
          
     }
     
     public int daysBetween(String d1, String d2){
         Date date1 = getDDMMYYYYAsDate(d1);        
        Date date2 = getDDMMYYYYAsDate(d2);
         return daysBetween(d1, d2);
     }
     
     /** Using Calendar - THE CORRECT WAY**/
    public static long daysBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        long daysBetween = 0;
        while (date.before(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }
    
    
     
     public Date getDDMMYYYYAsDate(String date){
         
         Date result = new Date();
         Calendar cal = new GregorianCalendar();                          
         cal.set(Calendar.YEAR, Integer.valueOf(date.substring(6,10)));
         cal.set(Calendar.MONTH, Integer.valueOf(date.substring(3,5)));
         cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date.substring(0,2)));
         cal.set(Calendar.HOUR, 0);
         cal.set(Calendar.MINUTE, 0);
         cal.set(Calendar.SECOND, 0);
         cal.set(Calendar.MILLISECOND, 0);
         
         cal.add(Calendar.MONTH, -1);
         result.setTime(cal.getTimeInMillis());
         return result;
     }
}

package org.szelc.app.antstock.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.szelc.app.antstock.settings.Settings;
import org.apache.log4j.Logger;
/**
 *
 * @author szelc.org
 */
public class SessionDaysUtil {

    private final List<Date> sessionDaysFreeList = new LinkedList();
    private final Set<Date> sessionDaysFreeSet = new HashSet();
    private final SimpleDateFormat dateFormatDDMMYY = new SimpleDateFormat("dd-MM-yy");
    private static final String SESSION_FILE_PATH_NAME = Settings.SESSION_FREE_DAYS_PATH_FILE_CSV;
    private static final Integer DAY_MILISECONDS = 24 * 60 * 60 * 1000;
    private static final Logger LOG = Logger.getLogger(SessionDaysUtil.class.toString());

    public List<Date> loadListFreeDaysFromFile(String filePathName) throws FileNotFoundException,
            IOException, ParseException {
        if (sessionDaysFreeList.isEmpty()) {
            FileReader fis = new FileReader(filePathName);
            BufferedReader br = new BufferedReader(fis);
            String line;
            while ((line = br.readLine()) != null) {
                sessionDaysFreeList.add(dateFormatDDMMYY.parse(line));
            }
        }
        return sessionDaysFreeList;
    }

    public Set<Date> loadSetFreeDaysFromFile(String filePathName) throws FileNotFoundException,
            IOException, ParseException {
        if (sessionDaysFreeSet.isEmpty()) {
            FileReader fis = new FileReader(filePathName);
            BufferedReader br = new BufferedReader(fis);
            String line;
            while ((line = br.readLine()) != null) {
                sessionDaysFreeSet.add(dateFormatDDMMYY.parse(line));
            }
        }
        return sessionDaysFreeSet;
    }

    public boolean isFreeDay(Date date) {
        Date dateFormatted;
        try {
            dateFormatted = dateFormatDDMMYY.parse(dateFormatDDMMYY.format(date));
        } catch (ParseException e) {
             e.printStackTrace();
            System.exit(0);
            return false;
        }
        Set<Date> freeDays;
        try {
            freeDays = loadSetFreeDaysFromFile(SESSION_FILE_PATH_NAME);
        } catch (FileNotFoundException ex) {
           ex.printStackTrace();
           System.exit(0);
            return false;
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
            System.exit(0);
            return false;
        }
        boolean result = freeDays.contains(dateFormatted) || dateFormatted.getDay() == 0 || dateFormatted.getDay() == 6;

        return result;
    }

    public Date getLastSessionDayWithExistStockQuotes(Date lastDate) {
        SimpleDateFormat df = new SimpleDateFormat("HHmm");
        if (Integer.valueOf(df.format(lastDate)) < 1830) {
            lastDate = new Date(lastDate.getTime() - DAY_MILISECONDS);
        }
        while (isFreeDay(lastDate)) {
            lastDate = new Date(lastDate.getTime() - DAY_MILISECONDS);
        }
        return lastDate;
    }
    

    public List<Date> getSessionDays(Date firstDate) {
        Date lastDate = new Date();
        return getSessionDays(lastDate);
    }
    
    public long getNumberDaysBetween(Date firstDate, Date lastDate) {
        System.out.println("getNumberDaysBetween date1 ["+dateFormatDDMMYY.format(firstDate)+"] date2 ["+dateFormatDDMMYY.format(lastDate)+"]");
        long milisecondsInDay = 24 * 60 * 60 * 1000;
        return (lastDate.getTime() - firstDate.getTime()) / milisecondsInDay;

    }

    public int getNumberStockDaysBetween(Date firstDate, Date lastDate) {
        int result = 0;
        Set<Date> sessionFreeDaysSet;
        long milisecondsInDay = 24 * 60 * 60 * 1000;
        try {
            LOG.info("LoadSetFree form file ["+SESSION_FILE_PATH_NAME+"]");
            sessionFreeDaysSet = loadSetFreeDaysFromFile(SESSION_FILE_PATH_NAME);
        } catch (IOException | ParseException ex) {
            sessionFreeDaysSet = new HashSet();
            LOG.error("Exception "+ex.getMessage());
           
            System.exit(0);
        }

        long firstDateTime = firstDate.getTime() + milisecondsInDay;
        
        LOG.info("start ["+firstDateTime+"] last ["+lastDate.getTime()+"] diff ["+(lastDate.getTime()-firstDateTime)+"]");
        for (long t = firstDateTime; t <= lastDate.getTime()+3600000; t += milisecondsInDay) {
            Date date = new Date(t);
            int nrDay = date.getDay();
            LOG.info("Number days ["+nrDay+"]");
            LOG.info("DATEstring" +date.toString() + "contains "+sessionFreeDaysSet.contains(date));
            if (sessionFreeDaysSet.contains(date) || nrDay == 0 || nrDay == 6) {
                continue;
            }
            LOG.info("PLUS PLUS");
            result++;
        }
        return result;
    }
    
    
    
    public List<Date> getSessionDays(Date firstDate, Date lastDate) {
        long milisecondsInDay = 24 * 60 * 60 * 1000;      
        List<Date> dates = new ArrayList();
        Set<Date> freeDaysSet;
        try {
            freeDaysSet = loadSetFreeDaysFromFile(SESSION_FILE_PATH_NAME);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            System.exit(0);
            return null;
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
            System.exit(0);
            return null;
        } 
        long firstDateTime = firstDate.getTime() + milisecondsInDay;
        long t;
        for ( t = firstDateTime; t <= lastDate.getTime(); t += milisecondsInDay) {
            Date date = new Date(t);
            int nrDay = date.getDay();          
            if (freeDaysSet.contains(date) || nrDay == 0 || nrDay == 6) {
                continue;
            }
            dates.add(date);
        }        
        return dates;
    }

  
}

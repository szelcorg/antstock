package org.szelc.app.antstock.service;

import org.szelc.app.antstock.util.FileUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.data.quotes.DayCompanyQuote;
import org.szelc.app.antstock.factory.CompanyServiceFactory;

import org.szelc.app.antstock.repository.QuoteRepository;
import org.szelc.app.antstock.settings.Settings;
import org.szelc.app.antstock.util.SessionDaysUtil;
import org.szelc.app.antstock.util.string.StringUtil;


/**
 *
 * @author szelc.org
 */
public final class QuoteService {
    private static final Logger log = Logger.getLogger(QuoteService.class.toString());
            
    private  QuoteRepository quoteRepository;
    private static QuoteService instance;
    
    
    public Float getCourse(String company){
        if(quoteRepository==null){
            quoteRepository = getQuoteRepository();
        }
        return quoteRepository.getLastQuotes(company).getCourse();
    }

    public QuoteRepository loadQuotesDataContainer(Set<String> companyList) {
        QuoteRepository result = new QuoteRepository();
        if (companyList == null) {
            log.error("Can't load quotes because company list is empty");
            return null;
        }
        for (String company : companyList) {
            //log.info("LOAD QUOTE FOR COMPANY ["+company+"]");
            List<DayCompanyQuote> dcqList = loadQuotes(company);
            if (dcqList == null) {         
                continue;
            }
            for (int i = 0; i < dcqList.size() - 1; i++) {
                dcqList.get(i + 1).setReference(dcqList.get(i).getCourse());
                result.addDayCompanyQuote(dcqList.get(i));
            }
            try{
                result.addDayCompanyQuote(dcqList.get(dcqList.size() - 1));
            }
            catch(ArrayIndexOutOfBoundsException e){
                log.info("ArrayIndex ["+company+"]");
            }

        }
        return result;
    }

    public List<DayCompanyQuote> loadQuotes(String companyName) {
        List<DayCompanyQuote> result = new ArrayList();
        List<String> lines ;
        try {
            //log.info("SETTINGS QUOTE PATH ["+Settings.QUOTE_FOLDER_FOR_UNPACK_ZIP+"]");
            if(CompanyService.isObsolete(companyName)){
                return result;
            }
            lines = Files.readAllLines(
                    Paths.get(Settings.QUOTE_FOLDER_FOR_UNPACK_ZIP + companyName + ".mst"), Charset.defaultCharset());
            
        } catch (java.nio.file.NoSuchFileException e) {
            log.warn("Nie istnieje plik z danymi dla sþółki ["+e.getMessage()+"]");
             log.error(e);
                //System.exit(0);
            return result;
        } catch (IOException e) {
            log.error(e);
                //System.exit(0);
           return result;
        }
        lines.remove(0);
     
        for (String line : lines) {
            DayCompanyQuote dcq = buildDayCompanyQuote(line);
            if(dcq==null){
                continue;
            }
            result.add(dcq);
        }
        return result;
    }
    
    public List<DayCompanyQuote> getDayCompanyQuote(String companyName, Date date) {
        boolean loadLastIfNull = false;
        if (date == null) {
            loadLastIfNull = true;
            SessionDaysUtil sdu = new SessionDaysUtil();
            date = sdu.getLastSessionDayWithExistStockQuotes(new Date());
            try {
                date = Settings.QUOTE_TABLE_VIEW_DATE_FORMAT.parse(Settings.QUOTE_TABLE_VIEW_DATE_FORMAT.format(date));
            } catch (ParseException e) {
                 log.error(e);
                //System.exit(0);
            }
        }
        if (StringUtil.isSet(companyName)) {
            List<DayCompanyQuote> res = new ArrayList();
            res.add(getQuoteRepository().getDayQuote(companyName, date, loadLastIfNull));
            return res;

        } else {
            return getQuoteRepository().getDayQuotesListByDate(date);
        }

    }

    private DayCompanyQuote buildDayCompanyQuote(String line) {
        String[] tabs = line.split(",");     
        Date date = null;
        try {
            date = Settings.QUOTE_FILE_DATE_FORMAT.parse(tabs[1]);           
        } catch (ParseException ex) {
             log.error(ex);             
             //System.exit(0);             
        }
         DayCompanyQuote dcq;
         try{
            dcq = new DayCompanyQuote(tabs[0], date, Float.valueOf(tabs[2]), Float.valueOf(tabs[3]), Float.valueOf(tabs[4]),
             Float.valueOf(tabs[5]), Long.valueOf(tabs[6]));
         }
         catch(NumberFormatException e){
             log.info("number format exception for ["+tabs[0]+"]");
             return null;
         }
        return dcq;
    }
    
    private QuoteService(){
     
    }
    
    public static QuoteService instance() {
        if (instance == null) {
            instance = new QuoteService();
        }
        return instance;
    }
    
    public QuoteRepository getQuoteRepository(){
         return getQuoteRepository(CompanyServiceFactory.instance()
                 .getCompanyService().getCompanyEvaluated(true));
    }
     
    public List<DayCompanyQuote> getDayQuotesList(Set<String> companySet, Date date){
        List<DayCompanyQuote> result = new ArrayList();
        QuoteRepository repo = getQuoteRepository();
        
        companySet.forEach((company) -> {
            result.add(repo.getDayQuote(company, date, true));
        });                
        return result;
    }

    public QuoteRepository getQuoteRepository(Set<String> companySet) {
        //log.info("getQuoteRepository size ["+companySet.size()+"]");        
        if (quoteRepository == null) {                
            long time1 = System.currentTimeMillis();
             if (existNotDownloadedQuotes()) {
                    dowloadStockQuotesFile();
             }   
             quoteRepository = loadQuotesDataContainer(companySet);
            log.info("TIME [loading transactions from file] miliseconds ["+(System.currentTimeMillis() - time1+"]"));
        }     
        return quoteRepository;
    }

    /**
     * Download stock quotes from net.
     */
    public void dowloadStockQuotesFile() {
        log.info("Downloading file ..");
        if (FileUtil.downloadFileFromURL(Settings.QUOTE_FOR_DOWNLOAD_FILE_ZIP_URL, Settings.QUOTE_DOWNLOANDED_FILE_ZIP)) {
            FileUtil.unpackZipFile(Settings.QUOTE_DOWNLOANDED_FILE_ZIP, Settings.QUOTE_FOLDER_FOR_UNPACK_ZIP);
            log.info("File downloaded");
        } else {
            log.error("Can't download file");
        }

    }
  
    private Date getLastSessionDay() {
        try {
            List<DayCompanyQuote> list = loadQuotes(Settings.QUOTE_LAST_DAY_SESSION_TEST_COMPANY);
            return list.get(list.size() - 1).getDate();
        }
         catch (NullPointerException e) {
              log.error(e);
               //System.exit(0);
            return null;
        }
    }
    
    public Float getLastCourse(String company){
        DayCompanyQuote dcq = getLastQuote(company);
        if(dcq==null){
            return 0f;
        }
        return getLastQuote(company).getCourse();
    }
    
    public DayCompanyQuote getLastQuote(String company) {
        return loadQuotes(company).stream().reduce((a, b) -> b).orElse(null);        
    }

    public List<DayCompanyQuote> getLastQuote(String company, int numberLastQuote){
        company = company.toUpperCase();
         try {
            List<DayCompanyQuote> list = loadQuotes(company);
            if(numberLastQuote==1000000){
                return list;
            }
            int fromIndex = list.size()  - numberLastQuote;
            if(fromIndex<0){
                fromIndex = 0;
            }
            int toIndex = list.size();
            if(toIndex<=fromIndex){
                return null;
            }
            
            return list.subList(fromIndex, toIndex);
        }  catch (NullPointerException e) {
             log.error(e);
                //System.exit(0);
            return null;
        }
    }

    private Date getTodayDateTime(){
        try {
            return Settings.TODAY_DATETIME_FORMAT.parse(Settings.TODAY_DATETIME_FORMAT.format(new Date()));
        } catch (ParseException e) {
             log.error(e);
                //System.exit(0);
            return null;
        }
    }
    
      private Date getTodayDate(){
        try {
            return Settings.TODAY_DATE_FORMAT.parse(Settings.TODAY_DATE_FORMAT.format(new Date()));
        } catch (ParseException e) {
            log.error(e);
           // System.exit(0);
            return null;
        }
    }
    
    public boolean existNotDownloadedQuotes() {
//        if(true){
//            return true;
//        }
       
        Date lastDessionDate = getLastSessionDay();
        log.info("LastSessionDay ["+lastDessionDate+"]");
        if (lastDessionDate == null) {
            log.info("Brak jakichkolwiek notowań");
            return true;
        }
        Date toDate = getTodayDate();

        if (lastDessionDate.compareTo(toDate) == 0) {
            log.info("Notowania aktualne - dzisiejsze.");
            return false;
        }

        SessionDaysUtil sdu = new SessionDaysUtil();
        log.info("Today ["+toDate+"] lastSessionDay ["+lastDessionDate+"]");
        int numberSessionDaysBetween = sdu.getNumberStockDaysBetween(lastDessionDate, toDate);
        if (numberSessionDaysBetween == 0) {
            log.info("Notowania aktualne. Dzisiaj jest dzień wolny.");
            return false;
        }
        log.info("Beetween ["+numberSessionDaysBetween+"]");
      
        if (numberSessionDaysBetween == 1) {
            long daysBetween = sdu.getNumberDaysBetween(lastDessionDate, toDate);
            log.info("Liczba dni od ostatniej sesji do dzisiaj ["+daysBetween+"]");
            if (daysBetween > 1 && sdu.isFreeDay(toDate)){ 
                log.info("Brak aktualnych notowań. Od ostatnich notowań wystąpił dzień wolny.");
                return true;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
            boolean result = Integer.valueOf(sdf.format(getTodayDateTime())) > 1825;
            if(result){
                log.info("Brak aktualnych notowań. Jest po 18:25 - można pobrać aktualne notowania");
            }
            else {
                log.info("Brak aktualnych notowań. Jest przed 18:25 - nie można jeszcze pobrać aktualnych notowania");
            }
            return result;
        }
        log.info("Brak aktualnych notowań z conajmniej jednego dnia");
        return true;
    }


}

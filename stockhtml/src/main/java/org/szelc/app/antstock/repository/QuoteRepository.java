package org.szelc.app.antstock.repository;

import org.szelc.app.antstock.data.quotes.DayCompanyQuote;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.factory.QuoteServiceFactory;

/**
 *
 * @author szelc.org
 */
public class QuoteRepository {

    private static final Logger LOG = Logger.getLogger(QuoteRepository.class.getName());
    private static final int NUMBER_FIELDS = 7;
    private Map<Date, List<DayCompanyQuote>> dayQuotesMap = new HashMap();
    private Map<String, List<DayCompanyQuote>> companyQuotesMap = new HashMap();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public void addDayCompanyQuote(DayCompanyQuote quote) {
        if (!dayQuotesMap.containsKey(quote.getDate())) {
            dayQuotesMap.put(quote.getDate(), new ArrayList());
        }
        dayQuotesMap.get(quote.getDate()).add(quote);

        if (!companyQuotesMap.containsKey(quote.getCompanyName())) {
            companyQuotesMap.put(quote.getCompanyName(), new ArrayList());
        }
        companyQuotesMap.get(quote.getCompanyName()).add(quote);
    }

    public List<String> getSessionDatesYYYYMMDD(String companyName) {
        List<DayCompanyQuote> dcqList = getCompanyQuotesMap().get(companyName);       
        List<String> result = new ArrayList();
        dcqList.forEach((dcq) -> {
            result.add(dateFormat.format(dcq.getDate()));
        });
        return result;
    }

    public List<DayCompanyQuote> getDayQuotesListByDate(Date date) {
        LOG.info("etDayQuotesListByDate date ["+date+"]");
        return dayQuotesMap.get(date);
    }

    public List<DayCompanyQuote> getDayQuotesListByDateStr(String date) {
        try {
            return dayQuotesMap.get(dateFormat.parse(date));
        } catch (ParseException ex) {
            LOG.error(ex);
            System.exit(0);
            return new ArrayList();
        }
    }

    public List<DayCompanyQuote> getDayQuotesList(String companyName) {
        return companyQuotesMap.get(companyName.toUpperCase());
    }

    public List<DayCompanyQuote> getDayQuotesList(String company, Date dateFrom, Date dateTo) {        
        if(companyQuotesMap.containsKey(company)){            
            return companyQuotesMap.get(company).stream().filter(t->!t.getDate().before(dateFrom)&&!t.getDate().after(dateTo)).collect(Collectors.toList());            
        }        
        //LOG.warn("Brak danych notowań dla spółki ["+company+"]");
        return new ArrayList();        
    }
    
    public DayCompanyQuote getDayQuote(String company, final Date date, boolean loadLastIfNull) {
//         System.out.println("company ["+company+"] date ["+date+"]");        
        Optional<DayCompanyQuote> dcqOptional;
        //Dla daty
        if (companyQuotesMap.get(company) != null) {
            dcqOptional = companyQuotesMap.get(company).stream().filter(dcq -> dcq.getDate().equals(date)).findAny();
            if (dcqOptional.isPresent()) {
                return dcqOptional.get();
            }
        }
        //Jesli nie ma dla daty to ostatni rekord
        List<DayCompanyQuote> dcqList = companyQuotesMap.get(company);
        if (dcqList != null && dcqList.size() > 0) {
            dcqOptional = dcqList.stream().skip(dcqList.size() - 1).findAny();
            if (dcqOptional.isPresent()) {
                return dcqOptional.get();
            }
        }

        dcqList = QuoteServiceFactory.instance().getQuoteService().loadQuotes(company);

        if (dcqList == null || dcqList.size() < 1) {
            return null;
        }
        dcqOptional = dcqList.stream().filter(dcq -> dcq.getDate().equals(date)).findAny();
        if (!dcqOptional.isPresent()) {
            dcqOptional = dcqList.stream().skip(dcqList.size() - 1).findAny();
        }
        if (dcqOptional.isPresent()) {
            return dcqOptional.get();
        }
        return null;
    }
   

    public Map<Date, List<DayCompanyQuote>> getDayQuotesMap() {
        return dayQuotesMap;
    }

    public void setDayQuotesMap(Map<Date, List<DayCompanyQuote>> dayQuotesMap) {
        this.dayQuotesMap = dayQuotesMap;
    }

    public Map<String, List<DayCompanyQuote>> getCompanyQuotesMap() {
        return companyQuotesMap;
    }

    public void setCompanyQuotesMap(Map<String, List<DayCompanyQuote>> companyQuotesMap) {
        this.companyQuotesMap = companyQuotesMap;
    }

    public int getNumberCompanies() {
        return companyQuotesMap.keySet().size();
    }

    public int getNumberFields() {
        return QuoteRepository.NUMBER_FIELDS;
    }

    public DayCompanyQuote getLastQuotes(String company) {
        //log.info("Company ["+company+"]");
        try {
            List<DayCompanyQuote> l = getCompanyQuotesMap().get(company);
            return l.get(l.size() - 1);
        } catch (java.lang.NullPointerException e) {
            //log.warn("Brak kursu dla spółki [" + company + "]");
            DayCompanyQuote dcq = new DayCompanyQuote();            
            dcq.setCompanyName(company.toUpperCase());
            dcq.setCourse(0f);
            return dcq;
        }
    }
}

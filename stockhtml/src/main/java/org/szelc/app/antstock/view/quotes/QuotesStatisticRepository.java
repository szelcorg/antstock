package org.szelc.app.antstock.view.quotes;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.szelc.app.antstock.data.quotes.DayCompanyQuote;
import org.szelc.app.antstock.factory.QuoteServiceFactory;
import org.szelc.app.antstock.repository.QuoteRepository;

/**
 *
 * @author szelc.org
 */
public class QuotesStatisticRepository {

    private final Map<String, QuotesStatistic> statisticsMap = new HashMap();
    private Set<String> companySet;
    
    private Date dateFrom;
    private Date dateTo;
   

    public QuotesStatisticRepository(QuoteRepository qdc) {
        build(qdc);
    }

    public QuotesStatisticRepository(QuoteRepository qdc, Date dateFrom, Date dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        build(qdc);
    }
    
    public QuotesStatisticRepository(Set<String> companySet, QuoteRepository qdc, Date dateFrom, Date dateTo) {
        this.companySet = companySet;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        build(qdc);
    }
     
    public QuotesStatistic getStatistic(String companyName) {
        return statisticsMap.get(companyName);
    }
    
    public List<QuotesStatistic> getStatisticList(){
        List<QuotesStatistic> result = new ArrayList<>();
        Iterator<QuotesStatistic> it =  statisticsMap.values().iterator();
        while(it.hasNext()){
            result.add(it.next());
        }
        return result;
    }

    private void build(QuoteRepository qdc) {        
        if(companySet == null){
            companySet = new HashSet();
            companySet.addAll(qdc.getCompanyQuotesMap().keySet());
        }
        for (String company : companySet) {  
            //System.out.println("Searching for ["+company+"] ["+dateFrom+"] ["+dateTo+"]");
            List<DayCompanyQuote> dcqList;
            if(dateFrom!=null && dateTo!=null){
                dcqList = qdc.getDayQuotesList(company, dateFrom, dateTo);
            }
            else {
                dcqList = qdc.getDayQuotesList(company);
            }
            if (!dcqList.isEmpty()) {
                statisticsMap.put(company, getUpDownEqualQuotes(company, dcqList));
            }

        }
    }

    public QuotesStatistic getUpDownEqualQuotes(String company, List<DayCompanyQuote> dcqList) {
        QuotesStatistic result;
        if(dcqList.isEmpty()){
            return new QuotesStatistic(company, dateFrom, dateTo, 
                    0, 0, 0, 0, 0f, 0f, 0f, 0f, 0f);
        }
        DayCompanyQuote dcqCurr = dcqList.get(dcqList.size() - 1);
        Float priceToday = dcqCurr.getCourse();
        int up = 0, down = 0, equal = 0, numbers = 0;
        float closePriceTotal = 0;
        float minClose = 1000000f, maxClose = 0f;
        for (DayCompanyQuote dcq : dcqList) {
            numbers++;
            Float price = dcq.getCourse();
            closePriceTotal += price;
            if (priceToday > price) {
                up++;
            } else if (priceToday < price) {
                down++;
            } else {
                equal++;
            }

            if (price < minClose) {
                minClose = price;
            } else if (price > maxClose) {
                maxClose = price;
            }
            
        }
        Date dateFrom = dcqList.get(0).getDate();
        Date dateTo = dcqList.get(dcqList.size() - 1).getDate();
        result = new QuotesStatistic(company, dateFrom, dateTo, numbers, up, down, equal, minClose, maxClose,
        dcqList.get(0).getCourse(), dcqList.get(dcqList.size()-1).getCourse(), QuoteServiceFactory.instance().getQuoteService().getLastCourse(company));
        result.setAvgArithmeticClose(numbers > 0 ? closePriceTotal / numbers: 0f);
        return result;
    }
}

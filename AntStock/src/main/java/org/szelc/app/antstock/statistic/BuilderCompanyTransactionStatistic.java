package org.szelc.app.antstock.statistic;

import org.apache.log4j.Logger;


import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.szelc.app.antstock.data.Transaction;
import org.szelc.app.antstock.repository.QuoteRepository;
import org.szelc.app.antstock.repository.TransactionRepository;

/**
 *
 * @author ms
 */
public class BuilderCompanyTransactionStatistic {
    
    private final List<CompanyTransactionStatistic> companyTransactionStatistics = new LinkedList();
    private final Set<String> companies;
    private final TransactionRepository transactionRepository;
   // private final DaySession daySession;
    QuoteRepository quoteRepository;
    private final Logger log = Logger.getLogger("BuilderCompanyTransactionStatistic");

    public BuilderCompanyTransactionStatistic(Set<String> companies, TransactionRepository transactionRepository,
            QuoteRepository quoteRepository) {
        this.companies = companies;
        this.transactionRepository = transactionRepository;
  
        this.quoteRepository = quoteRepository;
    }

    public List<CompanyTransactionStatistic> buildCompanyTransactionsStatistic(String dateFrom, String dateTo) {
        log.debug("buildCompanyTransactionsStatistic companySize ["+companies.size()+"]");
        for (String company : companies) {
       
            CompanyTransactionStatistic statistic = buildCompanyTransactionStatistic(company, dateFrom, dateTo);
            if (statistic != null) { 
                log.trace("Add statistic for company ["+company+"]");
                companyTransactionStatistics.add(
                        statistic
                );
            } else {
               log.warn("Statistic is null for company ["+company+"]");
            }
        }

        return companyTransactionStatistics;
    }

    private CompanyTransactionStatistic buildCompanyTransactionStatistic(String company,
            String dateFrom, String dateTo) {
        CompanyTransactionStatistic companyStatistic
                = new CompanyTransactionStatistic(company, dateFrom, dateTo);

        List<Transaction> result;
        if (dateFrom == null) {
            result = transactionRepository.getTransactionList(company);
            // log.info("----Count transaction ["+result.size()+"]");
        } else {
            result = transactionRepository.getTransactionList(company);
           // result = transactionRepository.getTransactionDataList(company, dateFrom, dateTo);
           
        }
        log.trace("--------------ALL TRANSACTIONS SIZE ["+result.size()+"]");
        if (result == null || result.isEmpty()) {
            //System.out.println("Result is null");;
            return null;
        }

        companyStatistic.build(result,  quoteRepository);
        return companyStatistic;
    }
}

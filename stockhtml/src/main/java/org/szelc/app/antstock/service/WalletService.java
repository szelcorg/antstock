package org.szelc.app.antstock.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.szelc.app.antstock.factory.QuoteServiceFactory;
import org.szelc.app.antstock.factory.TransactionServiceFactory;
import org.szelc.app.antstock.repository.QuoteRepository;
import org.szelc.app.antstock.repository.TransactionRepository;

import org.szelc.app.antstock.statistic.CompanyTransactionStatistic;
import org.szelc.app.antstock.statistic.StockDataBuilder;

/**
 *
 * @author szelc.org
 */
public class WalletService {

    private static final Logger log = Logger.getLogger(WalletService.class.toString());

    private final StockDataBuilder stockDataBuilder = new StockDataBuilder();
    
     public List<CompanyTransactionStatistic> loadStockWalletData(boolean reload) {
         long time1 = System.currentTimeMillis();
        Set<String> companies = TransactionServiceFactory.instance().
                getTransactionService().getTransactionRepository(reload).getAllCompanies();

        List<CompanyTransactionStatistic> transactionStatistics
                = buildStatisticTransaction(companies,
                        TransactionServiceFactory.instance().
                        getTransactionService().getTransactionRepository(),
                        QuoteServiceFactory.instance().
                        getQuoteService().getQuoteRepository(companies)
                );

        log.debug("TIME [loading wallet from file] miliseconds [" + (System.currentTimeMillis() - time1 + "]"));
        return transactionStatistics;
     }

    public List<CompanyTransactionStatistic> loadStockWalletData() {
        long time1 = System.currentTimeMillis();
        Set<String> companies = TransactionServiceFactory.instance().
                getTransactionService().getTransactionRepository().getAllCompanies();

        List<CompanyTransactionStatistic> transactionStatistics
                = buildStatisticTransaction(companies,
                        TransactionServiceFactory.instance().
                        getTransactionService().getTransactionRepository(),
                        QuoteServiceFactory.instance().
                        getQuoteService().getQuoteRepository(companies)
                );

        log.debug("TIME [loading wallet from file] miliseconds [" + (System.currentTimeMillis() - time1 + "]"));
        return transactionStatistics;
    }

    public List<CompanyTransactionStatistic> buildStatisticTransaction(Set<String> companies,
            TransactionRepository transactionRepository, QuoteRepository quoteRepository) {

        List<CompanyTransactionStatistic> statistics = stockDataBuilder.buildTransactionsStatistic(
                companies,
                transactionRepository, quoteRepository);
        return statistics;
    }
}

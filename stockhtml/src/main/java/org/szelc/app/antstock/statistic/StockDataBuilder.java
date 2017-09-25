package org.szelc.app.antstock.statistic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.szelc.app.antstock.repository.QuoteRepository;
import org.szelc.app.antstock.repository.TransactionRepository;



/**
 *
 * @author ms
 */
public class StockDataBuilder {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
    public StockDataBuilder() {
    }

      public List<CompanyTransactionStatistic> buildTransactionsStatistic(
            Set<String> companies, TransactionRepository transactionRepository, QuoteRepository quoteRepository) {
          
        BuilderCompanyTransactionStatistic builderStatistic =
                new BuilderCompanyTransactionStatistic(companies, transactionRepository, quoteRepository);
        
        List<CompanyTransactionStatistic> transactionStatistics = builderStatistic.buildCompanyTransactionsStatistic("01-01-08", 
                sdf.format(new Date()));
        return transactionStatistics;
    }
}

package org.szelc.app.antstock.register;

import org.szelc.app.antstock.repository.QuoteRepository;
import org.szelc.app.antstock.repository.TransactionRepository;

/**
 *
 * @author szelc.org
 */
public class DataContainerRegister {

    private static TransactionRepository transactionDataContainer;
    private static QuoteRepository quotesDataContainer;

    public static TransactionRepository getTransactionDataContainer() {
        return transactionDataContainer;
    }

    public static void setTransactionDataContainer(TransactionRepository transactionDataContainer) {
        DataContainerRegister.transactionDataContainer = transactionDataContainer;
    }

    public static QuoteRepository getQuotesDataContainer() {
        return quotesDataContainer;
    }

    public static void setQuotesDataContainer(QuoteRepository quotesDataContainer) {
        DataContainerRegister.quotesDataContainer = quotesDataContainer;
    }
    
    
    
}

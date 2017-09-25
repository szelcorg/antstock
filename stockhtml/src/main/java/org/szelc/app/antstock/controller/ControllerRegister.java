package org.szelc.app.antstock.controller;

import org.szelc.app.antstock.view.quotes.QuotesViewController;
import org.szelc.app.antstock.view.transaction.TransactionViewController;

/**
 *
 * @author szelc.org
 */
public class ControllerRegister {

    private static QuotesViewController quotesViewController;
    private static TransactionViewController transactionViewController;

    public static QuotesViewController getQuotesViewController() {
        return quotesViewController;
    }

    public static void setQuotesViewController(QuotesViewController quotesViewController) {
        ControllerRegister.quotesViewController = quotesViewController;
    }

    public static TransactionViewController getTransactionViewController() {
        return transactionViewController;
    }

    public static void setTransactionViewController(TransactionViewController transactionViewController) {
        ControllerRegister.transactionViewController = transactionViewController;
    }
    
    
}
